import { Injectable } from '@angular/core';
import { Subject, Observable, timer } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class TypingService {
  private isTyping: boolean = false;

  startTyping() {
    if (!this.isTyping) {
      this.isTyping = true;
      this.notifyTypingStatus();

      // Assuming typing stops after 1000 milliseconds (adjust as needed)
      timer(1000)
        .pipe(switchMap(() => {
          this.isTyping = false;
          this.notifyTypingStatus();
          return [];
        }))
        .subscribe();
    }
  }

  private notifyTypingStatus() {
    this.typingStatus.next(this.isTyping);
  }

  private typingStatus = new Subject<boolean>();

  getTypingStatus(): Observable<boolean> {
    return this.typingStatus.asObservable();
  }
}
