import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {SignUpRequest} from "../dto/SignUpRequest";
import {NgIf} from "@angular/common";
import {UserService} from "../service/user-service.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent {
  signUpForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: UserService,private router:Router) {
    this.signUpForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.signUpForm.valid) {
      const signUpRequest: SignUpRequest = this.signUpForm.value;
      this.authService.signUpUser(signUpRequest).subscribe(()=>{
        this.router.navigate(['/']);
        }


      );
    }
  }

  public isControlInvalid(controlName: string): boolean {
    const control = this.signUpForm.get(controlName);
    return !!(control && control.invalid && control.dirty);
  }

}
