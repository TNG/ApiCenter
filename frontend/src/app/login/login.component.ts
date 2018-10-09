import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SessionService} from "../session.service";
import {Token} from "../models/token";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  loginError = false;

  constructor(private formBuilder: FormBuilder, private sessionService: SessionService, private router: Router) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  public onSubmit() {
    this.submitted = true;

    this.sessionService.login(this.loginForm.controls.username.value, this.loginForm.controls.password.value).subscribe((data: Token) => {
      if (data) {
        localStorage.setItem('token', data.token);

        this.router.navigate(['/']);
      }

      return data;
    }, error => {
      if (error.status == 401) {
        this.loginError = true;
      }
    });
  }

}
