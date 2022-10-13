import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../user.service';
import * as XLSX from "xlsx";
import { TokenStorageService } from '../token-storage.service';
import { Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
@Component({
  selector: 'app-reportuser',
  templateUrl: './reportuser.component.html',
  styleUrls: ['./reportuser.component.css']
})

export class ReportuserComponent implements OnInit {
  displayedColumns = ['sno','partnername', 'compensationplan', 'compensationmethodology','fromdate','todate'];
  plans:Object[]=this.tokenStorageService.getReport();
  dataSource: MatTableDataSource<Object>;
  
  hasData:boolean=true;
  @ViewChild(MatPaginator)
  paginator: MatPaginator = new MatPaginator(new MatPaginatorIntl(), ChangeDetectorRef.prototype);;
  @ViewChild(MatSort)
  sort: MatSort = new MatSort;
  
    constructor(public router:Router,public userService:UserService,public tokenStorageService:TokenStorageService) {
      
      this.dataSource = new MatTableDataSource(this.plans);
      
    }
    exportexcel(){
     
     const ws: XLSX.WorkSheet =XLSX.utils.json_to_sheet(this.plans);
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
    XLSX.writeFile(wb, "X1.xlsx");
     
      }
   ngOnInit(): void {
    let user=this.tokenStorageService.getUser();
    if(user===null){
      this.router.navigate(["/"]);
      alert("OOPS!!!! You are Logged Out...");
    }
    else if(user.role==="ROLE_REPORT" ){
      const observable= this.userService.findAll();
      observable.subscribe((res)=>{
        //window.sessionStorage.setItem("report",JSON.stringify(res));
       },
        (error)=>{
          if(error.status===401 || error.status===403){
            alert("You are Signout...");
            this.tokenStorageService.signOut();
            this.router.navigate(["/"]);
          }
        });
      }
      else{
        this.router.navigate([user.role.replace("ROLE_","").toLowerCase()+"home"]);
      }
    }
    ngAfterViewInit() {
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
}
