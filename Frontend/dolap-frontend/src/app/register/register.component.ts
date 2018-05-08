import { Component, OnInit } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import {NgForm} from '@angular/forms';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

   onSubmit(f:NgForm) {
  	console.log(f.value);
  }
}
