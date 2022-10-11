import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: any = {
    firstname: null,
    lastname: null,
    username:null,
    location:null,
    jobtitle:null,
    department:null,
    role:null,
    password: null
  };
  locations=["HYDERABAD","CHENNAI","BANGLORE","PUNE","MUMBAI","TRIVENDRUM"];
  jobtitles=["TRAINEE","ANALYST","ASSOCIATE","ENGINEER","MANAGER"];
  departments=["FINANCE","INSURANCE","HEALTHCARE","HUMANRESOURCE","SALES","MARKETING"];
  roles=["COMPENSATION","REPORT","ADMIN",]
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(public userService:UserService) { }

  ngOnInit(): void {
  }

  onSubmit(): void {

    const observable = this.userService.registerUser(this.form);
    observable.subscribe((res)=>{
      console.log("res"+JSON.stringify(res));
    },
    (error)=>{
      console.log("error"+JSON.stringify(error));
      if(error.status===200){
        alert("Regisration Success");
      }
      else {
        alert(error.error.text);
      }
    });
    
    
  }

}
