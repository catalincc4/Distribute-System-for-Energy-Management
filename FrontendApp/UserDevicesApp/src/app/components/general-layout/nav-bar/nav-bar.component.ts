import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../../../services/token-storage.service";
import {TokenService} from "../../../services/token.service";

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit{
  userDetail : string = '';
  constructor(
    private tokenStorageService: TokenStorageService,
    private tokenService: TokenService
    ){}
  ngOnInit() {
    const token = this.tokenStorageService.getToken();
    if(token !== null) {
      const decodeToken = this.tokenService.getDecodedAccessToken(token);
      this.userDetail = decodeToken.user.firstname + ' ' + decodeToken.user.lastname;
    }
  }

  logout(){
    this.tokenStorageService.signOut();
  }
}
