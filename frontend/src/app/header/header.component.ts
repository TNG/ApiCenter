import {Component, OnInit} from '@angular/core';
import {LoginEvent} from '../login.event';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  username: string;


  constructor(private loginEvent: LoginEvent) {
  }

  ngOnInit() {
    const username = localStorage.getItem('username');

    if (username) {
      this.username = username;
    }

    this.loginEvent.getValue().subscribe((token) => this.username = token.username);
  }

}
