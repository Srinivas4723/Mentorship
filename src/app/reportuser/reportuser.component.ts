import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import * as XLSX from "xlsx";
import { TokenStorageService } from '../token-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reportuser',
  templateUrl: './reportuser.component.html',
  styleUrls: ['./reportuser.component.css']
})
export class ReportuserComponent implements OnInit {
  plans:any=[];
  tablesize:number=5;
  tablesizes:any=[1,5,10,15,20,25,50,100];
  currentpage=1;
  hasdata:boolean=false;
  constructor(public router:Router,public userService:UserService,public tokenStorageService:TokenStorageService) { }

  onTableSizeChange(event:any){
    this.currentpage=1;
    this.tablesize=event.target.value;
  }
  pagechange(event:any){
    this.currentpage=event;
  }
  loadPlans(){
    const observable= this.userService.findAll();
    observable.subscribe((res)=>{
      console.log("x"+res);
      this.plans=res;
      if(this.plans.length>0){
        this.hasdata=true;
      }
    },
    (error)=>{
      if(error.status===401 || error.status===401){
        alert("You are Signout...");
          this.tokenStorageService.signOut();
          this.router.navigate(["/"]);
      }
    });
  }
  exportexcel(){
    let tb=this.tablesize;
    let tcp=this.currentpage;
    this.tablesize=this.plans.length;
    this.currentpage=1;
    let element = document.getElementById('excel-table');
    const ws: XLSX.WorkSheet =XLSX.utils.table_to_sheet(element);
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
    XLSX.writeFile(wb, "X1.xlsx");
    this.tablesize=tb;
    this.currentpage=tcp;
    }
    ngOnInit(): void {
      let user=this.tokenStorageService.getUser();
      if(user.id===null){
        this.router.navigate(["/"]);
      }
      else if(user.roles[0]==="ROLE_REPORT" ){
        this.loadPlans();
      }
      else{
        this.router.navigate([user.roles[0].replace("ROLE_","").toLowerCase()+"home"]);
      }
    }

}
