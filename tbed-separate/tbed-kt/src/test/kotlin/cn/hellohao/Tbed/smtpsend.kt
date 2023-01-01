package cn.hellohao.Tbed

import com.sun.mail.smtp.SMTPAddressFailedException
import com.sun.mail.smtp.SMTPAddressSucceededException
import com.sun.mail.smtp.SMTPSendFailedException
import com.sun.mail.smtp.SMTPTransport
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */ /**
 * Demo app that shows how to construct and send an RFC822
 * (singlepart) message.
 *
 * XXX - allow more than one recipient on the command line
 *
 * This is just a variant of msgsend.java that demonstrates use of
 * some SMTP-specific features.
 *
 * @author Max Spivak
 * @author Bill Shannon
 */
object smtpsend {
    /**
     * Example of how to extend the SMTPTransport class.
     * This example illustrates how to issue the XACT
     * command before the SMTPTransport issues the DATA
     * command.
     *
     * public static class SMTPExtension extends SMTPTransport {
     * public SMTPExtension(Session session, URLName url) {
     * super(session, url);
     * // to check that we're being used
     * System.out.println("SMTPExtension: constructed");
     * }
     *
     * protected synchronized OutputStream data() throws MessagingException {
     * if (supportsExtension("XACCOUNTING"))
     * issueCommand("XACT", 250);
     * return super.data();
     * }
     * }
     */
    @JvmStatic
    fun main(argv: Array<String>) {
        val to: String
        var subject: String? = null
        var from: String? = null
        var cc: String? = null
        var bcc: String? = null
        var url: String? = null
        var mailhost: String? = null
        val mailer = "smtpsend"
        var file: String? = null
        var protocol: String? = null
        var host: String? = null
        var user: String? = null
        var password: String? = null
        var record: String? = null // name of folder in which to record mail
        var debug = false
        var verbose = false
        var auth = false
        var prot = "smtp"
        val `in` = BufferedReader(InputStreamReader(System.`in`))
        var optind: Int

        /*
	 * Process command line arguments.
	 */optind = 0
        while (optind < argv.size) {
            if (argv[optind] == "-T") {
                protocol = argv[++optind]
            } else if (argv[optind] == "-H") {
                host = argv[++optind]
            } else if (argv[optind] == "-U") {
                user = argv[++optind]
            } else if (argv[optind] == "-P") {
                password = argv[++optind]
            } else if (argv[optind] == "-M") {
                mailhost = argv[++optind]
            } else if (argv[optind] == "-f") {
                record = argv[++optind]
            } else if (argv[optind] == "-a") {
                file = argv[++optind]
            } else if (argv[optind] == "-s") {
                subject = argv[++optind]
            } else if (argv[optind] == "-o") { // originator
                from = argv[++optind]
            } else if (argv[optind] == "-c") {
                cc = argv[++optind]
            } else if (argv[optind] == "-b") {
                bcc = argv[++optind]
            } else if (argv[optind] == "-L") {
                url = argv[++optind]
            } else if (argv[optind] == "-d") {
                debug = true
            } else if (argv[optind] == "-v") {
                verbose = true
            } else if (argv[optind] == "-A") {
                auth = true
            } else if (argv[optind] == "-S") {
                prot = "smtps"
            } else if (argv[optind] == "--") {
                optind++
                break
            } else if (argv[optind].startsWith("-")) {
                println(
                    "Usage: smtpsend [[-L store-url] | [-T prot] [-H host] [-U user] [-P passwd]]"
                )
                println(
                    "\t[-s subject] [-o from-address] [-c cc-addresses] [-b bcc-addresses]"
                )
                println(
                    "\t[-f record-mailbox] [-M transport-host] [-d] [-a attach-file]"
                )
                println(
                    "\t[-v] [-A] [-S] [address]"
                )
                System.exit(1)
            } else {
                break
            }
            optind++
        }
        try {
            /*
	     * Prompt for To and Subject, if not specified.
	     */
            if (optind < argv.size) {
                // XXX - concatenate all remaining arguments
                to = argv[optind]
                println("To: $to")
            } else {
                print("To: ")
                System.out.flush()
                to = `in`.readLine()
            }
            if (subject == null) {
                print("Subject: ")
                System.out.flush()
                subject = `in`.readLine()
            } else {
                println("Subject: $subject")
            }

            /*
	     * Initialize the Jakarta Mail Session.
	     */
            val props = System.getProperties()
            if (mailhost != null) props["mail.$prot.host"] = mailhost
            if (auth) props["mail.$prot.auth"] = "true"

            /*
	     * Create a Provider representing our extended SMTP transport
	     * and set the property to use our provider.
	     *
	    Provider p = new Provider(Provider.Type.TRANSPORT, prot,
		"smtpsend$SMTPExtension", "Jakarta Mail demo", "no version");
	    props.put("mail." + prot + ".class", "smtpsend$SMTPExtension");
	     */

            // Get a Session object
            val session = Session.getInstance(props, null)
            if (debug) session.debug = true

            /*
	     * Register our extended SMTP transport.
	     *
	    session.addProvider(p);
	     */

            /*
	     * Construct the message and send it.
	     */
            val msg: Message = MimeMessage(session)
            if (from != null) msg.setFrom(InternetAddress(from)) else msg.setFrom()
            msg.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(to, false)
            )
            if (cc != null) msg.setRecipients(
                Message.RecipientType.CC,
                InternetAddress.parse(cc, false)
            )
            if (bcc != null) msg.setRecipients(
                Message.RecipientType.BCC,
                InternetAddress.parse(bcc, false)
            )
            msg.subject = subject
            val text = collect(`in`)
            if (file != null) {
                // Attach the specified file.
                // We need a multipart message to hold the attachment.
                val mbp1 = MimeBodyPart()
                mbp1.setText(text)
                val mbp2 = MimeBodyPart()
                mbp2.attachFile(file)
                val mp = MimeMultipart()
                mp.addBodyPart(mbp1)
                mp.addBodyPart(mbp2)
                msg.setContent(mp)
            } else {
                // If the desired charset is known, you can use
                // setText(text, charset)
                msg.setText(text)
            }
            msg.setHeader("X-Mailer", mailer)
            msg.sentDate = Date()

            // send the thing off
            /*
	     * The simple way to send a message is this:
	     *
	    Transport.send(msg);
	     *
	     * But we're going to use some SMTP-specific features for
	     * demonstration purposes so we need to manage the Transport
	     * object explicitly.
	     */
            val t = session.getTransport(prot) as SMTPTransport
            try {
                if (auth) t.connect(mailhost, user, password) else t.connect()
                t.sendMessage(msg, msg.allRecipients)
            } finally {
                if (verbose) println(
                    "Response: " +
                            t.lastServerResponse
                )
                t.close()
            }
            println("\nMail was sent successfully.")

            /*
	     * Save a copy of the message, if requested.
	     */if (record != null) {
                // Get a Store object
                var store: Store? = null
                if (url != null) {
                    val urln = URLName(url)
                    store = session.getStore(urln)
                    store.connect()
                } else {
                    store = if (protocol != null) session.getStore(protocol) else session.store

                    // Connect
                    if (host != null || user != null || password != null) store.connect(
                        host,
                        user,
                        password
                    ) else store.connect()
                }

                // Get record Folder.  Create if it does not exist.
                val folder = store.getFolder(record)
                if (folder == null) {
                    System.err.println("Can't get record folder.")
                    System.exit(1)
                }
                if (!folder!!.exists()) folder.create(Folder.HOLDS_MESSAGES)
                val msgs = arrayOfNulls<Message>(1)
                msgs[0] = msg
                folder.appendMessages(msgs)
                println("Mail was recorded successfully.")
            }
        } catch (e: Exception) {
            /*
	     * Handle SMTP-specific exceptions.
	     */
            if (e is SendFailedException) {
                var sfe = e as MessagingException
                if (sfe is SMTPSendFailedException) {
                    val ssfe = sfe
                    println("SMTP SEND FAILED:")
                    if (verbose) println(ssfe.toString())
                    println("  Command: " + ssfe.command)
                    println("  RetCode: " + ssfe.returnCode)
                    println("  Response: " + ssfe.message)
                } else {
                    if (verbose) println("Send failed: $sfe")
                }
                var ne: Exception?
                while (sfe.nextException.also { ne = it } != null &&
                    ne is MessagingException) {
                    sfe = ne as MessagingException
                    if (sfe is SMTPAddressFailedException) {
                        val ssfe = sfe
                        println("ADDRESS FAILED:")
                        if (verbose) println(ssfe.toString())
                        println("  Address: " + ssfe.address)
                        println("  Command: " + ssfe.command)
                        println("  RetCode: " + ssfe.returnCode)
                        println("  Response: " + ssfe.message)
                    } else if (sfe is SMTPAddressSucceededException) {
                        println("ADDRESS SUCCEEDED:")
                        val ssfe = sfe
                        if (verbose) println(ssfe.toString())
                        println("  Address: " + ssfe.address)
                        println("  Command: " + ssfe.command)
                        println("  RetCode: " + ssfe.returnCode)
                        println("  Response: " + ssfe.message)
                    }
                }
            } else {
                println("Got Exception: $e")
                if (verbose) e.printStackTrace()
            }
        }
    }

    /**
     * Read the body of the message until EOF.
     */
    @Throws(IOException::class)
    fun collect(`in`: BufferedReader): String {
        var line: String?
        val sb = StringBuffer()
        while (`in`.readLine().also { line = it } != null) {
            sb.append(line)
            sb.append("\n")
        }
        return sb.toString()
    }
}