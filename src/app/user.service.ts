import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
const API_URL = 'https://2tozyz5mkf.execute-api.us-west-2.amazonaws.com/prod/';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  deletePlan(userid: any, planid: any) {
    return this.http.delete(API_URL+"plans/deleteplan/"+userid+"/"+planid);
  }
  saveMethod(methodform:any, userid:number,planid: number) {
    return this.http.post(API_URL+"plans/addmethod/"+userid+"/"+planid,methodform);
  }
  saveUserData(form: { empid: number; firstname: string; lastname: string; location: string; jobtitle: string; department: string; role: string; }) {
    return this.http.put(API_URL+"user/updateuser",form);
  }
  findUserData() {
    return this.http.get(API_URL+"user/userdata");
  }
  
  getMyPlans(userid:any) {
    return this.http.get(API_URL+"plans/myplans/userid/"+userid);
  }
  login(loginrequest:any) {
    return this.http.post(API_URL + 'user/signin', loginrequest);
  }

  registerUser(user:any){
    return this.http.post(API_URL+"user/register",user);
  }
  savePlan(PLAN: any,userid :any) {
    return this.http.post(API_URL+"plans/createplan/user/"+userid,PLAN);
  }
  deleteMethod(userid:any,planid: any, methodindex: number) {
    return this.http.delete(API_URL+"plans/deletemethod/"+userid+"/"+planid+"/"+methodindex);
  }
  findAll() {
    return this.http.get(API_URL+"plans/report");
   }
  constructor(public http:HttpClient) { }
}
