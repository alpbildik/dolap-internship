import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {NgForm} from '@angular/forms';
import {AppComponent} from '../app.component';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

 	constructor(private http : HttpClient) { }

	ngOnInit() {
 		this.appComponent = AppComponent;
  	}

  	onSubmit(f:NgForm) {
		console.log(f.value);
    	const req = this.http.post("http://localhost:8080/user/login", {
      		email: f.value.email,
      		password: f.value.password
    		}).subscribe(
        		res => {
		        	if (res.token) {
                console.log(this.appComponent);
             		this.appComponent._loggedIn =true;
		        		this.appComponent._token = res.token;
                $route.reload();

		        	}
        		},
        		err => {
          			console.log("Error occured");
        		}
      	);
    
	}
}