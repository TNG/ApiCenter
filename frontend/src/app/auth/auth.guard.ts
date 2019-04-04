import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import { AuthenticationService } from './auth.service';

@Injectable()
export class AuthenticationGuard implements CanActivate {

  constructor(private router: Router, private auth: AuthenticationService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.auth.isTokenValid()) {
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }

}
