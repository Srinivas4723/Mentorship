import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { faWindowClose } from '@fortawesome/free-solid-svg-icons';
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
  roles=["COMPENSATION","REPORT","ADMIN"]
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(public router:Router,public userService:UserService) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.form.firstname=this.form.firstname.trim();
    this.form.lastname=this.form.lastname.trim();
    this.form.username=this.form.username.trim();
    this.form.password=this.form.password.trim();
    if(this.form.firstname!=="" || this.form.lastname!=="" || this.form.username!=="" || this.form.password!==""){
    const observable = this.userService.registerUser(this.form);
    observable.subscribe((res)=>{
      console.log("res"+JSON.stringify(res));
    },
    (error)=>{
      console.log("error"+JSON.stringify(error));
      if(error.status===200){
        alert("Regisration Success");
        this.isSuccessful=true;
        this.router.navigate(['/']);
      }
      else {
        alert(error.error);
      }
    });
  }
    
  }

}
