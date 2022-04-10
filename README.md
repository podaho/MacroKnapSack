#Macro Nutrient Knap Sack Filler Web Service

##How to Run
###To obtain the USDA api key:
- Go to https://platform.fatsecret.com/api/Default.aspx?screen=r and apply for a API access on FatSecretPlatform API website
- Paste the provided clientID and clientSecret under ```spring.security.oauth2.client.registration.fat-secret.client-id``` and ```spring.security.oauth2.client.registration.fat-secret.client-secret``` in ```src/main/resources/application.yml```
- Upon confirming and activating your subscription, you must whitelist your IPv4 or IPv6 address in the Allowed IP Addresses section of the page

###To access swagger locally:
- Upon successful compilation of project go to http://localhost:8080/swagger-ui/index.html

