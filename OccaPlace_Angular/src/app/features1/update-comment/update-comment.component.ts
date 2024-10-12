import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CommentaireDto} from "../../core/dto/CommentaireDto";

@Component({
  selector: 'app-update-comment',
  templateUrl: './update-comment.component.html',
  styleUrl: './update-comment.component.css'
})
export class UpdateCommentComponent {
  @Input() isOpen: boolean = false;
  @Input() comment: CommentaireDto | null = null;
  @Output() save = new EventEmitter<string>();
  @Output() cancel = new EventEmitter<void>();

  editedComment: string = '';

  ngOnChanges() {
    if (this.comment) {
      this.editedComment = this.comment.contenu;
    }
  }

  onSave() {
    this.save.emit(this.editedComment);
  }

  onCancel() {
    this.cancel.emit();
  }
}
