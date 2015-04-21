package email;

import models.Contact;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;
import play.twirl.api.Html;

public class SendEmail {

	private static String msg = "Envio de email - Contato";
	private static String emailAdmin = "nilda.docetentacao@gmail.com";
	
	public static void newContact(Contact contact){
		final Email email = new Email();
		
		email.setSubject("Email de contato");
		email.setFrom(contact.getEmail());
		email.addTo(emailAdmin);
		Html render = views.html.email_contact.render(msg, contact);
		email.setBodyText(contact.getNome());
		email.setBodyHtml(render.body());

		MailerPlugin.send(email);
	}
}
