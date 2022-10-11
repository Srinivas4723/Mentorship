import { Component } from '@angular/core';
import { UserService } from './user.service';
import { TokenStorageService } from './token-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'compensationsystem';
  username="Srinvas Yadala";
  isLoggedIn:Boolean=true;
  constructor(public userService: UserService,public tokenStorageService:TokenStorageService){}
  logout(){
    this.tokenStorageService.signOut();
    window.location.reload();
  }
}