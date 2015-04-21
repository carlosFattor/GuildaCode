package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Contact;
import play.Routes;
import play.data.Form;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import email.SendEmail;

public class Application extends Controller {

	private static Pattern pattern;
	private static Matcher matcher;
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static Form<Contact> contForm = Form.form(Contact.class);
	private static String fileName = "EmailContact.txt"; 
	
    public static Result index() {
        return ok(views.html.index.render());
    }
    
    //@BodyParser.Of(BodyParser.Json.class)
    public static Result subscribe(String email)throws IOException{
    	File file;
    	if(validateEmail(email)){
    		File f = new File(fileName);
    		if(!f.exists()){
    			f.createNewFile();
    		}
    		try (PrintStream out = new PrintStream(new FileOutputStream(fileName, true))) {
    			StringBuilder sb = new StringBuilder();
    			sb.append(email).append("\r\n");
    		    out.print(sb.toString());
    		    return ok();
    		} 
    	}else{
    		return badRequest();
    	}
    }
    
    public static Promise<Result> contact(){
    	Form<Contact> frmFromRequest = contForm.bindFromRequest();
    	if(frmFromRequest.hasErrors()){
    		return F.Promise.pure(badRequest(views.html.index.render()));
    	}
    	
    	Contact contact = frmFromRequest.get();
    	
    	Runnable task = () -> {
    		sendEmail(contact);
    	};
    	new Thread(task).start();
    	return F.Promise.pure(ok());
    }

    public static void sendEmail(Contact contact){
    	SendEmail.newContact(contact);
    }
    
    public static Result javascriptRoutes(){
    	response().setContentType("text/javascript");
    	return ok(
    			Routes.javascriptRouter("myJsRoutes", 
    					routes.javascript.Application.subscribe(),
    					routes.javascript.Application.contact()
    			)
    		);
    }
    
    private static boolean validateEmail(String email){
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
