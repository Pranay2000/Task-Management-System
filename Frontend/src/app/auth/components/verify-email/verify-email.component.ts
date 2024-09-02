import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent implements OnInit {

  constructor(private route: ActivatedRoute, private authService: AuthService, private snackbar: MatSnackBar, private router: Router) { } 

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (token) {
        this.authService.verifyEmail(token).subscribe({
          next: (response) => {
            console.log('Response:', response); // Add this line to debug
            if (response.status === 200) { 
              this.snackbar.open('Email verified successfully!', 'Close', { duration: 5000 });
              this.router.navigateByUrl("/login");
            } else {
              this.snackbar.open('Verification failed. Invalid or expired token.', 'Close', { duration: 5000 });
              this.router.navigateByUrl("/signup");
            }
          },
          error: (err) => {
            console.log(err);
            this.snackbar.open('Verification failed. Invalid or expired token.', 'Close', { duration: 5000 });
            this.router.navigateByUrl("/signup");
          }
        });
      }
    });
  }
}