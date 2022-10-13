import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from '../token-storage.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  userprofile:any;
  role:any;
  constructor(public tokenStorageService:TokenStorageService,public router:Router) { }

  ngOnInit(): void {
    let user=this.tokenStorageService.getUser();
    if(user===null){
      this.router.navigate(["/"]);
      alert("OOPS!!!! You are Logged Out...");
    }
    else{
      this.userprofile=user;
      this.role=this.userprofile.role.replace("ROLE_","");
    }
  }

}
