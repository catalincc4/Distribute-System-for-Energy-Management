import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {TokenStorageService} from "./services/token-storage.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  title = 'UserDevicesApp';
  isLoggedIn = false;

  constructor(
    private tokenStorageService: TokenStorageService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.isLoggedIn = this.tokenStorageService.getToken() != '';
    if(!this.isLoggedIn){
      this.router.navigate(['/login']);
    }
  }


}
