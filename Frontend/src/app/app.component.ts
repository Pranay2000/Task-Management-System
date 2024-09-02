import { Component } from '@angular/core';
import { StorageService } from './auth/services/storage/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent{
  isEmployeeLoggedIn: boolean = StorageService.isEmployeeLoggedIn();
  isAdminLoggedIn: boolean = StorageService.isAdminLoggedIn();
  showWelcomeMessage: boolean = true;

  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events.subscribe(event=> {
      this.isEmployeeLoggedIn = StorageService.isEmployeeLoggedIn();
      this.isAdminLoggedIn = StorageService.isAdminLoggedIn();
      if (this.isEmployeeLoggedIn || this.isAdminLoggedIn) {
        this.showWelcomeMessage = false;
      }
    })
  }

  hideWelcomeMessage() {
    this.showWelcomeMessage = false;
  }

  logout() {
    StorageService.logout();
    this.router.navigateByUrl("/login");
  }
}
