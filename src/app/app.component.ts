import { Component, OnInit } from '@angular/core';
import { UserService } from './user.service';
import { TokenStorageService } from './token-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  title = 'compensationsystem';
  isLoggedIn:Boolean=false;
  
  constructor(public userService: UserService,public tokenStorageService:TokenStorageService){
    let user=this.tokenStorageService.getUser();
    if(user!==null){
      this.isLoggedIn=true;
    }
  }
  logout(){
    this.tokenStorageService.signOut();
    window.location.reload();
  }
  ngOnInit(){
    let user=this.tokenStorageService.getUser();
    if(user!==null){
      this.isLoggedIn=true;
    }
  }
}