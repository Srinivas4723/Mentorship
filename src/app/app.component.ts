import { Component, OnInit } from '@angular/core';
import { UserService } from './user.service';
import { TokenStorageService } from './token-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  title = 'compensationsystem';
  isLoggedIn:Boolean=false;
  
  constructor(public router:Router,public userService: UserService,public tokenStorageService:TokenStorageService){
    let user=this.tokenStorageService.getUser();
    if(user!==null){
      this.isLoggedIn=true;
    }
  }
  logout(){
    this.tokenStorageService.signOut();
    this.router.navigate(["/"]);
    this.isLoggedIn=false;
    //window.location.reload();
  }
  ngOnInit(){
    let user=this.tokenStorageService.getUser();
    if(user!==null){
      this.isLoggedIn=true;
    }
    else{
      this.router.navigate(["/"]);
    }
  }
}