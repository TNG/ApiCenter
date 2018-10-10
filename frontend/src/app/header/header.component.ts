import {Component, OnInit} from '@angular/core';
import {LoginEvent} from '../login.event';
import {Router} from '@angular/router';
import {SessionToken} from '../models/sessiontoken';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  username: string;


  constructor(private loginEvent: LoginEvent, private router: Router) {
  }

  ngOnInit() {
    const username = localStorage.getItem('username');

    if (username) {
      this.username = username;
    }

    this.loginEvent.getValue().subscribe((token) => this.username = token.username);
  }

  public logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');

    this.loginEvent.changeValue(new SessionToken('', ''));

    this.router.navigate(['/login']);
  }

}
