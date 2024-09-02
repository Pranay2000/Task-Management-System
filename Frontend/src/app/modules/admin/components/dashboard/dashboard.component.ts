import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  listOfTasks: any = [];
  searchForm!: FormGroup;

  constructor(private adminService: AdminService, private snackbar: MatSnackBar, private fb: FormBuilder) { 
    this.getTasks();
    this.searchForm = this.fb.group({
      title: [null]
    })
  }

  getTasks() {
    this.adminService.getAllTasks().subscribe((res)=> {
      this.listOfTasks = res;
    })
  }

  deleteTask(id: number) {
    this.adminService.deleteTask(id).subscribe((res)=> {
      this.snackbar.open("Task Deleted Successfully", "Close", { duration: 5000 });
      this.getTasks();
    })
  }

  searchTask() {
    this.listOfTasks = [];
    const title = this.searchForm.get('title')!.value;
    console.log(title);
    this.adminService.searchTask(title).subscribe((res)=> {
      console.log(res);
      this.listOfTasks = res;
    })
  }
}
