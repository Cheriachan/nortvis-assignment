# Nortvis Assignment

Documentation for how to use the Nortvis assignment project for managing user and images.

## Installation

Download zip or clone code from [GitHub repository](https://github.com/Cheriachan/nortvis-assignment.git)

```bash
git clone https://github.com/Cheriachan/nortvis-assignment.git
```

Import the project into an IDE (preferable IntelliJ) and run the *AppApplications.java* class

#### Or

Run the application in command line. 

## Configurations and Architecture

There are no environment variables configured. All configurations are specified in application.yml file in the resource directory.
<br/>
1. The maximum file upload size for uploading images is configured in *spring.servlet.multipart.max-file-size* property in the _application.yml_ file
2. The datasource connection pool is h2 database
3. The application uses jwt authorization for users to perform operations after logging in.
4. Swagger documentation is integrated for easy access of application endpoints.
5. Three tier architecture design is employed
6. Unit testing is implemented for the _controller_, _service_, _persistence_ and _client_ modules

## Steps to follow

1. Run the application and access the [Swagger API documentation](http://localhost:8080/swagger-ui/index.html)
<br/>All endpoints except those under **Auth Controller** requires authorization to proceed<br/><br/>
2. Register the user
```
POST: /auth/register
```

It requires proper validation for the fields:

- All are mandatory fields
- Username must be from 8 to 20 characters
- Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters
- Email should be in the right format
- Phone number should be 10 digits

3. Once the user registration is complete, login user with credentials. The response body will have an access token
<br/><br/>
All operations hence forth requires authorization to proceed
4. Click on **Authorize** right above the endpoints listing to the right side of the page<br/><br/>
5. Paste the access token here and click on _close_ button<br/><br/>
6. __POST: /image/upload__ endpoint is used to uploading multipart files to the service which in turn sends it to _Imgur_ client and registers in the database associating it with the user. The response body will have the link which will display the image in a browser<br/><br/>
7. __GET: /image/list__ endpoint will list all images associated with the logged in user<br/><br/>
8. __DELETE: /image/{image_id}/delete endpoint is used for deleting a single image file<br/><br/>
9. __GET: /profile/info__ displays the registered user information along with the associated images