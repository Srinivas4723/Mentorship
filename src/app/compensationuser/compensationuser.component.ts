import { Component, OnInit } from '@angular/core';
import { UserService } from "../user.service"
import * as XLSX from "xlsx";
import { FormControl, Validators } from '@angular/forms';
import { TokenStorageService } from '../token-storage.service';
import { Router } from '@angular/router';

const PLAN={
    partnername:"",
    compensationplan:"",
    compensationmethodology:"",
    fromdate:"",
    todate:"",
    methods:[
      {
        minimum:"",
        maximum:"",
        percentage:""
      }
    ]
  };

@Component({
  selector: 'app-compensationuser',
  templateUrl: './compensationuser.component.html',
  styleUrls: ['./compensationuser.component.css']
})
export class CompensationuserComponent implements OnInit {

  form={
    partnername:"",
    compensationplan:"",
    compensationmethodology:"",
    fromdate:"",
    todate:"",
    minimum:"",
    maximum:"",
    percentage:""
  };
  plans:any=[];
  tablesize:number=5;
  tablesizes:any=[5,10,15,20];
  currentpage=1;
  // isAddingNewPlan:boolean=false;
  today:Date=new Date();
  tomorrow:any=new Date(this.today.setDate(this.today.getDate()+1));
  compensationmethodologies=["VOLUME","REVENUE"];
  user=this.tokenStorageService.getUser();
  constructor(public router:Router,public userService: UserService,public tokenStorageService:TokenStorageService){}
  makeFormNull(){
    this.form={
      partnername:"",
      compensationplan:"",
      compensationmethodology:"",
      fromdate:"",
      todate:"",
      minimum:"",
      maximum:"",
      percentage:""
    };
  }
  onTableSizeChange(event:any){
    this.currentpage=1;
    this.tablesize=event.target.value;
  }
  pagechange(event:any){
    this.currentpage=event;
  }
  
  exportexcel(){
    let element = document.getElementById('excel-table');
    const ws: XLSX.WorkSheet =XLSX.utils.table_to_sheet(element);
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
    XLSX.writeFile(wb, "X1.xlsx");
  }
  getMinToDate(){
    if(this.form.fromdate!==""){
      let tempfromdate=new Date(this.form.fromdate.toString());
      let temptodate= new Date(tempfromdate.setDate(tempfromdate.getDate()+1));
      return temptodate;
    }
    return this.tomorrow;
  }
  numberValidator(field:any){
    return new FormControl(field, Validators.min(1));//verify
  }
  requiredValidator(field:any){
    return new FormControl(field,Validators.required);
  }
  minLengthValidator(field:any){
    return new FormControl(field,Validators.minLength(3));
  }
  nameValidator(field:any){
   return new FormControl(field,Validators.pattern("[A-Za-z0-9]*"));
  }
  
  isValidPlan(){
    let errorcount=0;
    this.form.partnername=this.form.partnername.trim();
    this.form.compensationplan=this.form.compensationplan.trim();
    Object.entries(this.form).forEach(key=>{
      document.getElementById(key[0])?.classList.remove("error");
      let x=this.requiredValidator(key[1]).errors;
      if(x!==null){
        document.getElementById(key[0])?.classList.add("error");
        errorcount+=1;
      }
    });
    if(errorcount>0){
      alert("Please Enter All Mandatory Fields");return false;
    }
    if(this.minLengthValidator(this.form.partnername).errors!==null){
      alert("Partner Name Size cannot less than 3");
      document.getElementById("partnername")?.classList.add("error");
      return false;
    }
    if(this.nameValidator(this.form.partnername).errors!==null){
      alert("Partner Name do not accept special Characters");
      document.getElementById("partnername")?.classList.add("error");
      return false;
    }
    if(this.minLengthValidator(this.form.compensationplan).errors!==null){
      alert("Compensation Plan Size cannot be less than 3");
      document.getElementById("compensationplan")?.classList.add("error");
      return false;
    }
    if(this.nameValidator(this.form.compensationplan).errors!==null){
      alert("compensationplan do not accept special Characters");
      document.getElementById("compensationplan")?.classList.add("error");
      return false;
    }
    if(this.numberValidator(this.form.minimum).errors!==null){
      alert("Minimum Quantity cannot be less than 1");
      document.getElementById("minimum")?.classList.add("error");
      return false;
    }
    if(this.numberValidator(this.form.minimum).errors!==null){
      alert("Maximum Quantity cannot be less than 1");
      document.getElementById("maximum")?.classList.add("error");
      return false;
    }
    if(this.numberValidator(this.form.minimum).errors!==null){
      alert("Percentage cannot be less than 1");
      document.getElementById("percentage")?.classList.add("error");
      return false;
    }
    if(this.form.maximum<this.form.minimum){
      alert("Minium cannot be greater than maximum");
      document.getElementById("minimum")?.classList.add("error");
      document.getElementById("maximum")?.classList.add("error");
      return false;
    }
    if(this.form.fromdate<this.tomorrow || this.form.todate<this.form.fromdate){
      alert("From Date and To Date are not Valid");
      document.getElementById("fromdate")?.classList.add("error");
      document.getElementById("todate")?.classList.add("error");
      return false;
    }
    return true;
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
  getPlanObject(){
    PLAN.partnername=this.form.partnername;
    PLAN.compensationplan=this.form.compensationplan;
    PLAN.compensationmethodology=this.form.compensationmethodology;
    PLAN.fromdate=this.form.fromdate;
    PLAN.todate=this.form.todate;
    PLAN.methods[0].minimum=this.form.minimum;
    PLAN.methods[0].maximum=this.form.maximum;
    PLAN.methods[0].percentage=this.form.percentage;
    return PLAN;
  }
  savePlan(){
    if(this.user===null){      
      alert("Not Authorised to Create Plan");
      this.tokenStorageService.signOut();
      this.router.navigate(["/"]);
      //window.location.reload();
    }
    else if(this.isValidPlan()){
      
      const observable=this.userService.savePlan(this.getPlanObject(),this.user.id);
      observable.subscribe((res)=>{
        //console.log("res"+JSON.stringify(res));
      },
      (error)=>{
        console.log(error);
        if(error.status===400){
          alert("Plan Already Exists / Invalid Plan");
          document.getElementById("minimum")?.classList.add("error");
          document.getElementById("maximum")?.classList.add("error");
        }
        else if(error.status===200){
          alert("PLan Added Success");
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
  
  deleteplan(planid:any,methodindex:number){
    if(confirm("Are you sure, You want to Delete this Plan?")){
      
    const observable=this.userService.deletePlan(this.user.id,planid,methodindex);
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
      this.router.navigate(["/"]);
      alert("OOPS!!!! You are Logged Out...");
    }
    else if(user.role==="ROLE_COMPENSATION" ){
      this.loadPlans();
    }
    else{
      this.router.navigate([user.role.replace("ROLE_","").toLowerCase()+"home"]);
    }
  }
}
