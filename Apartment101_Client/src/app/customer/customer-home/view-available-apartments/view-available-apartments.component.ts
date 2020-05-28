import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';

import { Application } from './application'
import { Observable, Subscription } from 'rxjs';

import { User } from 'src/app/shared/models/user';

@Component({
  selector: 'app-view-available-apartments',
  templateUrl: './view-available-apartments.component.html',
  styleUrls: ['./view-available-apartments.component.css']
})
export class ViewAvailableApartmentsComponent implements OnInit {

  private _url: string = 'http://localhost:8080/Apartment101_Server';

  private headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  currentUser: User;
  apartments: Observable<any>;
  application: Observable<Application>;
  email;
  errorMsg; 
  success: number;
  list = new Array<number>();
  
  
  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.getApartments();
    this.currentUser = JSON.parse(sessionStorage.getItem("customer"));
  }

  getApartments(){
    this.apartments = this.http.get(this._url + '/ApartmentAPI/getApts');
  }

  newApp(apt: Apartment){
    this.sendPostRequest(apt).subscribe(
      res => this.email = res,
      error => this.errorMsg = error
    );
    this.list.push(apt.aptNo);
  }

  sendPostRequest(apt: Apartment): Observable<any>{
    const data = {
      status: 0,
      apartment: {
        aptType: apt.aptType, // apt_type 1B1Bath, 2B1Bath, 2B2Bath
        noOfRooms: apt.noOfRooms, //
        noOfBaths: apt.noOfBaths, //
        aptNo: apt.aptNo, //
        aptLevel: apt.aptLevel,//
        typeOfFlooring: apt.typeOfFlooring, // Laminate, Carpet, Wood, Tile, Linoleum
        availability: 1, // 
      },
      user: {
        username: null,
        password: null,
        email: this.currentUser.email,
        usertype: 'CUSTOMER'
      }
    }
 
    return this.http.post<any>(
      this._url + '/ApplicationAPI/newApp',
      JSON.stringify(data),
      {headers: this.headers, responseType: 'text' as 'json'}
    );


  }
}
