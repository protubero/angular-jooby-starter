import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Person} from '../model';


@Injectable()
export class PersonsService {

  constructor(private http: HttpClient) { }

  list(): Observable<Person[]> {
     console.log('Load person list');
     return this.http.get<Person[]>('/api/persons');
  }

  findById(id: number): Observable<Person>  {
     console.log('Getting person with id: ' + id);
     return this.http.get<Person>('/api/persons/' + id);
  }

  submit(person: Person): Observable<String> {
    console.log('Saving person ' + JSON.stringify(person));
    return this.http.post<String>('/api/persons', person);
  }

  remove(id: number) {
    console.log('Delete person with id ' + id);
    return this.http.delete('api/persons/' + id);
  }

  update(id: number, person: Person): Observable<Person>  {
    console.log('Update person with id ' + id);
    return this.http.patch<Person>('/api/persons/' + id, person);
  }

}
