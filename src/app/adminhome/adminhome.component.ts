import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../user.service';
import { TokenStorageService } from '../token-storage.service';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-adminhome',
  templateUrl: './adminhome.component.html',
  styleUrls: ['./adminhome.component.css']
})
export class AdminhomeComponent implements OnInit {

  displayedColumns = ['empid','firstname', 'lastname', 'location','department','jobtitle','role','Action'];
  userdata:any=this.tokenStorageService.getUserData();
  dataSource: MatTableDataSource<Object>;
  //editableRow:number=0;
  form={
    empid:0,
    firstname:"",
    lastname:"",
    location:"",
    jobtitle:"",
    department:"",
    role:""
  }
  locations=["HYDERABAD","CHENNAI","BANGLORE","PUNE","MUMBAI","TRIVENDRUM"];
  jobtitles=["TRAINEE","ANALYST","ASSOCIATE","ENGINEER","MANAGER"];
  departments=["FINANCE","INSURANCE","HEALTHCARE","HUMANRESOURCE","SALES","MARKETING"];
  roles=["COMPENSATION","REPORT","ADMIN"];
  isFormEnabled:Boolean=false;
  nameValidator=(ele:any)=>new FormControl(ele,[Validators.required,Validators.pattern("[A-Za-z0-9]*"),Validators.minLength(3)]);
  hasData:boolean=false;
  @ViewChild(MatPaginator)
  paginator: MatPaginator = new MatPaginator(new MatPaginatorIntl(), ChangeDetectorRef.prototype);;
  @ViewChild(MatSort)
  sort: MatSort = new MatSort;
  user:any;
    constructor(public router:Router,public userService:UserService,public tokenStorageService:TokenStorageService) {
      console.log(this.userdata);
      if(this.userdata!==null ){
        this.hasData=true;
      }
      this.dataSource = new MatTableDataSource(this.userdata);
    }
    iseditable(rowid:number){
      //console.log(rowid);
     
      return this.form.empid==rowid;
    }
    cancel(){
      this.isFormEnabled=false;
      this.form.empid=0;
      this.form.firstname="";
      this.form.lastname="";
      this.form.location="";
      this.form.jobtitle="";
      this.form.department="";
      this.form.role="";
    }
    editUser(row:any){
      //this.editableRow=rowid;
      this.isFormEnabled=true;
      this.form.empid=row.empid;
      this.form.firstname=row.firstname;
      this.form.lastname=row.lastname;
      this.form.location=row.location;
      this.form.jobtitle=row.jobtitle;
      this.form.department=row.department;
      this.form.role=row.role;
    }
    saveUser(row:any){
      if(this.isFormValid()){
      const observable= this.userService.saveUserData(this.form);
      observable.subscribe((res)=>{
        if(res===null){
          alert("SomeThing Went Wrong!!!!");
        }
        else{
          window.sessionStorage.setItem("userdata",JSON.stringify(res));
          this.isFormEnabled=false;
          this.form.empid=-1;
          this.router.navigate(["/login"]);
          //this.ngOnInit();
          //window.location.reload();
        }
      },(error)=>{
        if(error.status===401 || error.status===403){
          alert("You are Signout...");

            this.tokenStorageService.signOut();
            this.router.navigate(["/"]);
            window.location.reload();
        }
      });
    }
    }
    isFormValid(){
      //console.log(document.getElementsBy("li").length);
      return document.getElementsByClassName("error").length>0?false:true;
    }
   ngOnInit(): void {
     //if(this.hasData){
      this.user=this.tokenStorageService.getUser();
    if(this.user===null){
      //this.router.navigate(["/"]);
      //alert("OOPS!!!! You are Logged Out...");
      //window.location.reload();
    }
    else
     if(this.user.role==="ROLE_ADMIN" ){
      const observable= this.userService.findUserData();
      observable.subscribe((res)=>{
        //window.sessionStorage.setItem("report",JSON.stringify(res));
      },
      (error)=>{
        if(error.status===401 || error.status===403){
          alert("NOt Authorised");
            this.tokenStorageService.signOut();
            this.router.navigate(["/"]);
            window.location.reload();
        }
      });
    }
    // else{
    //   alert("NOt Authorised");
    //   this.router.navigate([this.user.role.replace("ROLE_","").toLowerCase()+"home"]);
    // }
    }
    ngAfterViewInit() {
      //if(this.hasData){
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      console.log(this.dataSource);
    //}
    }

}
