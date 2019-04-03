import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SessionService} from '../session.service';
import {ActivatedRoute, Router} from '@angular/router';
import {SessionToken} from '../models/sessiontoken';
import {LoginEvent} from '../login.event';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  submitted = false;
  loginError = false;
  redirectUrl: string;

  constructor(private formBuilder: FormBuilder, private sessionService: SessionService, private router: Router,
              private loginEvent: LoginEvent, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.redirectUrl = this.route.snapshot.queryParamMap.get('redirectUrl');

    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  public onSubmit() {
    this.submitted = true;

    this.sessionService.login(this.loginForm.controls.username.value, this.loginForm.controls.password.value)
      .subscribe((data: SessionToken) => {
        if (data) {
          localStorage.setItem('token', data.token);
          localStorage.setItem('username', data.username);

          this.loginEvent.changeValue(data);
          this.router.navigate([this.redirectUrl || '/']);
        }

        return data;
      }, error => {
        if (error.status === 401) {
          this.loginError = true;
        }
      });
  }

}
