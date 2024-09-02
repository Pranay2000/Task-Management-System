import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmployeeService } from '../../services/employee.service';
import { ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-view-task-details',
  templateUrl: './view-task-details.component.html',
  styleUrls: ['./view-task-details.component.scss']
})
export class ViewTaskDetailsComponent {

  taskId: number = this.activatedRoute.snapshot.params["id"];
  taskData: any;
  commentForm!: FormGroup;
  comments: any;

  constructor(private employeeService: EmployeeService, private activatedRoute: ActivatedRoute, private fb: FormBuilder, private snackbar: MatSnackBar) { }

  ngOnInit() {
    this.getTaskById();
    this.getComments();
    this.commentForm = this.fb.group({
      content: [null, Validators.required]
    })
  }

  getTaskById() {
    this.employeeService.getTaskById(this.taskId).subscribe((res)=> {
      this.taskData = res;
    })
  }

  getComments() {
    this.employeeService.getCommentsByTask(this.taskId).subscribe((res)=> {
      this.comments = res;
    })
  }

  postComment() {
    this.employeeService.createComment(this.taskId, this.commentForm.get("content")?.value).subscribe((res)=> {
      if(res.id != null) {
        this.snackbar.open("Comment Posted Successfully!", "Close", { duration: 5000 });
        this.getComments();
      } else {
        this.snackbar.open("Something Went Wrong!", "Close", { duration: 5000 });
      }
    })
  }
}
