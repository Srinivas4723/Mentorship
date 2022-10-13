import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from '../token-storage.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  user: any;
  role:any;

  constructor(public router:Router,private userService: UserService, private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
    this.user=this.tokenStorage.getUser();
    if (this.user!==null) {
      this.isLoggedIn = true;
      
      this.router.navigate([this.user.role.replace("ROLE_","").toLowerCase()+"home"]);
      
    }
  }

  onSubmit(): void {
    
    this.form.username=this.form.username.trim();
    this.form.password=this.form.password.trim();
    if(this.form.username==="" || this.form.password===""){
      alert("UserName / Password Cannot be Blank");
    }
    else{
    this.userService.login(this.form).subscribe(
      (data:any) => {
        console.log(JSON.stringify(data));
        this.tokenStorage.saveToken(data.accesstoken);
        this.tokenStorage.saveUser(data);

        // this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.role = this.tokenStorage.getUser().role;
        const role=this.role.replace("ROLE_","").toLowerCase();
        console.log(this.role+"home");
        if(role==="report"){
            const observable= this.userService.findAll();
            observable.subscribe((res)=>{
              window.sessionStorage.setItem("report",JSON.stringify(res));
              window.location.reload();
            },
            (error)=>{
              if(error.status===401 || error.status===401){
                alert("You are Signout...");
                  this.tokenStorage.signOut();
                  this.router.navigate(["/"]);
              }
            });
        }
        else if(role==="admin"){
          const observable= this.userService.findUserData();
            observable.subscribe((res)=>{
              window.sessionStorage.setItem("userdata",JSON.stringify(res));
              window.location.reload();
            },
            (error)=>{
              if(error.status===401 || error.status===401){
                alert("You are Signout...");
                  this.tokenStorage.signOut();
                  this.router.navigate(["/"]);
              }
            });
        }
        //this.reloadPage();
        this.router.navigate([this.role.replace("ROLE_","").toLowerCase()+"home"]);
        
      },
      (error:any) => {
        //this.errorMessage = error.error.message;
        console.log("eror"+error);
        this.isLoginFailed = true;
      }
    );
    }
  }

 
}
