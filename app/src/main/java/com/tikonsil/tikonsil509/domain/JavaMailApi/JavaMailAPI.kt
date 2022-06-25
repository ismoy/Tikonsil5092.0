package com.tikonsil.tikonsil509.domain.JavaMailApi

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.tikonsil.tikonsil509.utils.constants.CredentialsJavaMailApi.TIKONSILEMAIL
import com.tikonsil.tikonsil509.utils.constants.CredentialsJavaMailApi.TIKONSILPASSWORD
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/** * Created by ISMOY BELIZAIRE on 09/05/2022. */
@Suppress("DEPRECATION")
class JavaMailAPI(context: Context, email: String, subject: String, message: String) :
 AsyncTask<Void?, Void?, Void?>() {
 @SuppressLint("StaticFieldLeak")
 private val context: Context = context
 private var session: Session? = null
 private val email: String = email
 private val subject: String = subject
 private val message: String = message
 @Deprecated("Deprecated in Java")
 override fun doInBackground(vararg voids: Void?): Void? {
  val properties = Properties()
  properties["mail.smtp.host"] = "smtp.gmail.com"
  properties["mail.smtp.socketFactory.port"] = "465"
  properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
  properties["mail.smtp.auth"] = "true"
  properties["mail.smtp.port"] = "465"
  session = Session.getDefaultInstance(properties, object : Authenticator() {
   override fun getPasswordAuthentication(): PasswordAuthentication {
    return PasswordAuthentication(TIKONSILEMAIL, TIKONSILPASSWORD)
   }
  })
  val mimeMessage = MimeMessage(session)
  try {
   mimeMessage.setFrom(InternetAddress(TIKONSILEMAIL))
   mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress(email).toString())
   mimeMessage.subject = subject
   mimeMessage.setText(message)
   Transport.send(mimeMessage)
  } catch (e: MessagingException) {
   e.printStackTrace()
  }
  return null
 }

}
