
import { Observable, of } from 'rxjs';
import { Session } from 'src/app/features/sessions/interfaces/session.interface';

export class SessionApiServiceMock {
    all(): Observable<Session[]> {
      const sessions: Session[] = [
        { id: 1, name: 'Session 1', date: new Date(), description: 'Description 1',users: [101, 102, 103], teacher_id: 201, createdAt:new Date, updatedAt:new Date },
        { id: 1, name: 'Session 2', date: new Date(), description: 'Description 2',users: [101, 102, 103, 104], teacher_id: 202, createdAt:new Date, updatedAt:new Date },
      ];

      return of(sessions);
    }

    private pathService = 'api/session';

    detail(): Observable<Session[]> {
        const sessions: Session[] = [];
  
        return of(sessions);
      }
   
  
    
}
  