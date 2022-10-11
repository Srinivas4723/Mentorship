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
  roles: string[] = [];

  constructor(public router:Router,private userService: UserService, private tokenStorage: TokenStorageService) { }

  ngOnInit(): void {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
      this.router.navigate([this.roles[0].replace("ROLE_","").toLowerCase()+"home"]);
      
    }
  }

  onSubmit(): void {
    

    this.userService.login(this.form).subscribe(
      (data:any) => {
        console.log(JSON.stringify(data));
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getUser().roles;
        console.log(this.roles[0],this.roles[0].replace("ROLE_","").toLowerCase()+"home");
        this.router.navigate([this.roles[0].replace("ROLE_","").toLowerCase()+"home"]);
        //this.reloadPage();
      },
      (error:any) => {
        //this.errorMessage = error.error.message;
        console.log("eror"+error);
        this.isLoginFailed = true;
      }
    );
  }

  reloadPage(): void {
    //window.location.reload();
  }

}
