justcoin-ripple-checker
=======================

Small tool to check that all money have reached juscoin wallet when they were sent from ripple.

#How to use
1. To build execute: `mvn clean package`
2. Modify the following attributes in settings.properties file: ripple.destination.tag, ripple.address, time.zone ([more info about timezone](http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTimeZone.html#forID(java.lang.String))) , start and end time
3. CD to target directory: `cd target`
4. Start an app: `java -jar justcoin-ripple-transaction-checker.jar full_path_to_your_settings_file/settings.properties`

By default app prints stucked transactions to a console using the following format:  
[logger-header] [when transactions was sent from ripple network] [amount and currency] [hash that you can check [here](http://www.ripplecharts.com/#/graph/6F101A75C5A915366954F3D78E8EEB70A01441F9E59752C552BB7ED65F63BF98)]

example:
```
23:08:57.461 [main] INFO  com.shinydev.Checker - 2014-08-23T14:10:40.000+02:00 TakerCurrency{currency=XRP, issuer=null, value=31.580739} B1653D0023538D9D7A23172625B76199182D08A8634AB0D2E3045ED48CCA94BD
23:08:57.461 [main] INFO  com.shinydev.Checker - 2014-08-23T13:51:50.000+02:00 TakerCurrency{currency=BTC, issuer=rJHygWcTLVpSXkowott6kzgZU6viQSVYM1, value=0.01170951} 77F8905129340B525442335870D62B09AE150CD166317630934018AFA6BC2CF4
23:08:57.461 [main] INFO  com.shinydev.Checker - 2014-08-23T08:01:10.000+02:00 TakerCurrency{currency=XRP, issuer=null, value=0.17824} 0D2DEFE5AB1AA74E9202572E77E48C2EC8155715D48298B62DF3D6521AD64C3E
```
 


