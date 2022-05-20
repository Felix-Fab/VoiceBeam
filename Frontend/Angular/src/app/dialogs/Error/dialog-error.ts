import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";

@Component({
    selector: 'dialog-error',
    templateUrl: 'dialog-error.html'
})

export class DialogError {
  title = "";
  message = "";

  constructor(@Inject(MAT_DIALOG_DATA) public data: any){
    this.title = data.title;
    this.message = data.message;
  }
}