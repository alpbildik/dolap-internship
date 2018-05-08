import { Component } from '@angular/core';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css', './css/main.css', './css/animate.css']
})
export class AppComponent {  
	title = 'Dolap staj frontend';

	
    private _token   : String  = "";
    private _loggedIn: boolean = false;

    isLoggedIn(){
        return this._loggedIn;
    }


    setLoggedIn(loggedIn){
        this._loggedIn = loggedIn;
    }

    getToken(){
        return this._token;
    }

    setToken(token){
        this._token = token;
    }

}
