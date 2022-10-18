import { Component, OnInit } from '@angular/core';
import { UserService } from "../user.service"
import * as XLSX from "xlsx";
import { FormControl, Validators } from '@angular/forms';
import { TokenStorageService } from '../token-storage.service';
import { Router } from '@angular/router';
import { fa1 } from '@fortawesome/free-solid-svg-icons';



@Component({
  selector: 'app-compensationuser',
  templateUrl: './compensationuser.component.html',
  styleUrls: ['./compensationuser.component.css']
})
export class CompensationuserComponent implements OnInit {
  methodform={
    minimum:"",
    maximum:"",
    percentage:""
  };
  minimum=(ele:any)=>new FormControl(ele, [Validators.min(1),Validators.required]);
  minmax=(min:any,max:any)=>new FormControl(min, Validators.max(<number><unknown>max));
  nameValidator=(ele:any)=>new FormControl(ele,[Validators.required,Validators.pattern("[A-Za-z0-9]*"),Validators.minLength(3)]);
  requiredValidator=(ele:any)=>new FormControl(ele,Validators.required);
  stringValidator=(ele:any)=>new FormControl(ele,[Validators.pattern("[A-Za-z0-9]*"),Validators.minLength(3)]);
  form={
    partnername:"",
    compensationplan:"",
    compensationmethodology:"",
    fromdate:"",
    todate:"",
    methods:[
      {minimum:"",
    maximum:"",
    percentage:""}
    ]
  };
  plans:any=[];
  tablesize:number=5;
  tablesizes:any=[5,10,15,20];
  currentpage=1;
  hasaddingNewMethod:number=-1;
  isMethodFormSubmitted:Boolean=false;
  isPlanFormSubmitted:Boolean=false;
  // isAddingNewPlan:boolean=false;
  today:any=new Date();
  todayiso:any= new Date().toISOString().split("T")[0];
  tomorrow:any=new Date(this.today.setDate(this.today.getDate()+1)).getDate();
  compensationmethodologies=["VOLUME","REVENUE"];
  user=this.tokenStorageService.getUser();
  constructor(public router:Router,public userService: UserService,public tokenStorageService:TokenStorageService){}
  makeMethodFormNull(){
    this.methodform={
      minimum:"",
      maximum:"",
      percentage:""
    }
  }
  makeFormNull(){
    this.form={
      partnername:"",
      compensationplan:"",
      compensationmethodology:"",
      fromdate:"",
      todate:"",
      methods:[
        {minimum:"",
      maximum:"",
      percentage:""}
    ]
    };
  }
  getRowSpan(rowspan:number,i:number){
    return this.hasaddingNewMethod===i?rowspan+1:rowspan;
  }
  addFormMethodRow(){
    this.form.methods.push({minimum:"",maximum:"",percentage:""});
    console.log(this.form);
  }
  removeRow(i:number){
    //this.form.methods.slice(0,i).+this.form.methods.slice(i+1);
    if(this.form.methods.length!==1)
      this.form.methods.splice(i,1);
    //this.form.methods.
  }
  addMethodRow(i:number){
    if(this.hasaddingNewMethod!==i)
      this.isMethodFormSubmitted=false;
    this.makeMethodFormNull();
    this.hasaddingNewMethod=i;
  }
  removeMethodRow(){
    this.makeFormNull();
    this.hasaddingNewMethod=-1;
    this.isMethodFormSubmitted=false;
  }
  saveMethod(planid:number){
    
       this.isMethodFormSubmitted=true;
    if(this.isMethodFormValid()){
      
      console.log(this.methodform,this.minimum(this.methodform.minimum));
      const observable= this.userService.saveMethod(this.methodform,this.user.id,planid);
      observable.subscribe((res)=>{},
      err=>{
        if(err.status==200){
          alert("Plan Added Success");
          //window.location.reload();
          this.makeFormNull();
          this.hasaddingNewMethod=-1;
          this.isMethodFormSubmitted=false;
          this.isPlanFormSubmitted=false;
          this.loadPlans();
        }
        else if(err.status==400){
          alert("Invalid Method");
          document.getElementById("minimum")?.classList.add("error");
          document.getElementById("maximum")?.classList.add("error");
        }
        else if(err.status==401 || err.status===403){
          alert("Your are logged out!!!");
          this.tokenStorageService.signOut();
          this.router.navigate(["/"]);
          window.location.reload();
        }
      });
    }
  }
 
  onTableSizeChange(event:any){
    this.currentpage=1;
    this.tablesize=event.target.value;
  }
  pagechange(event:any){
    this.currentpage=event;
  }
  
  getMinToDate(){
    if(this.form.fromdate!==""){
      let tempfromdate=new Date(this.form.fromdate.toString());
      let temptodate= new Date(tempfromdate.setDate(tempfromdate.getDate()+1));
      return temptodate;
    }
    return this.tomorrow;
  }
  isFormValid(){
    console.log(document.getElementsByClassName("error").length);
    return document.getElementsByClassName("error").length>0?false:true;
  }
  isMethodFormValid(){
    let ec=0;
    Object.entries(this.methodform).forEach(key=>{
      if(this.minimum(key[1]).errors){
        ec+=1;
      }
    });
    if(ec==0)
      return this.minmax(this.methodform.minimum,this.methodform.maximum).errors?false:true;
    return false;
    //return document.getElementsByClassName("alert alert-danger").length>0?false:true;
  }
  loadPlans(){
    const observable= this.userService.getMyPlans(this.user.id);
    observable.subscribe((res)=>{
      this.plans=res;
    },
    (error)=>{
      if(error.status===403 || error.status==401){
        this.tokenStorageService.signOut();
        this.router.navigate(["/"]);
        //window.location.reload();
      }
    });
  }
 
  savePlan(){
    this.isPlanFormSubmitted=true;
    console.log(this.form.fromdate>this.tomorrow );
    if(this.user===null){      
      alert("Not Authorised to Create Plan");
      this.tokenStorageService.signOut();
      this.router.navigate(["/"]);
      //window.location.reload();
    }
    // else if(this.form.fromdate<= this.todayiso|| this.form.todate<this.form.fromdate){
    //   this.isPlanFormSubmitted=true;
    //   alert("Invalid Dates Selected");
    //   document.getElementById("fromdate")?.classList.add("error");
    //   document.getElementById("today")?.classList.add("error");
    // }
    else  if(this.isFormValid() && this.form.fromdate> this.todayiso|| this.form.todate>this.form.fromdate){
      
      const observable=this.userService.savePlan(this.form,this.user.id);
      observable.subscribe((res)=>{
        //console.log("res"+JSON.stringify(res));
      },
      (error)=>{
        console.log(error);
        if(error.status===400){
          alert("Plan Already Exists / Invalid Plan");
          let fI=error.error;
          for(let i=0;i<fI.length;i++){       
          document.getElementById("minimum"+fI[i])?.classList.add("error");
          document.getElementById("maximum"+fI[i])?.classList.add("error");
          }
        }
        else if(error.status===200){
          console.log(error.error);
          alert("PLan Added Success");
          this.isPlanFormSubmitted=false;
          this.makeFormNull();
        }
        else if(error.status===403 || error.status==401){
          alert("OOPS!!!! You are Logged Out...");
          this.tokenStorageService.signOut();
          this.router.navigate(["/"]);
          //window.location.reload();
        }
        this.loadPlans();
       //window.location.reload();
      });
    }
  }
  deleteplan(planid:any){
    if(confirm("Are you sure, You want to Delete this Plan?")){
      
      const observable=this.userService.deletePlan(this.user.id,planid);
      observable.subscribe((res)=>{
          console.log("res"+res);
        },
        (error)=>{
          console.log(error);
          this.loadPlans();
          //window.location.reload();
        });
      }
  }
  deleteMethodRow(planid:any,methodindex:number){
    if(confirm("Are you sure, You want to Delete this Method?")){
      
    const observable=this.userService.deleteMethod(this.user.id,planid,methodindex);
    observable.subscribe((res)=>{
        console.log("res"+res);
      },
      (error)=>{
        this.loadPlans();
      });
    }
  }
  ngOnInit(): void { 
    //console.log("j"+this.user);
    let user=this.tokenStorageService.getUser();
    if(user===null){
      // this.router.navigate(["/"]);
      // alert("OOPS!!!! You are Logged Out...");
    }
    else
     if(user.role==="ROLE_COMPENSATION" ){
      this.loadPlans();
    }
    // else{
    //   this.router.navigate([user.role.replace("ROLE_","").toLowerCase()+"home"]);
    // }
  }
}
