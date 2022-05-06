import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";

@Component({
    selector: 'dialog-warning',
    templateUrl: 'dialog-warning.html'
  })
  export class DialogWarning {

    title = "";
    message = "";

    constructor(@Inject(MAT_DIALOG_DATA) public data: any){
      this.title = data.title;
        this.message = data.message;
    }
  }