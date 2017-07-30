import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';


export interface Person {
  id: number;
  name: string;
  email: string;
  age: number;
}

@Injectable()
export class PersonsService {

  constructor(private http: HttpClient) { }

  list(): Observable<Person[]> {
     return this.http.get<Person[]>('/api/persons');
  }

  findById(id: number): Observable<Person>  {
     return this.http.get<Person>('/api/persons/' + id);
  }

  submit(person: Person): Observable<String> {
    return this.http.post('/api/persons', person);
  }

  remove(id: number) {
    return this.http.delete('api/persons/' + id);
  }

  update(id: number, person: Person): Observable<Person>  {
    return this.http.patch<Person>('/api/persons/' + id, person);
  }

}
