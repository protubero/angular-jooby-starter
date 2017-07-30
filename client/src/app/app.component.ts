import { Component, OnInit } from '@angular/core';
import {DropdownModule, SelectItem, MenubarModule, MenuItem} from 'primeng/primeng';
import {HttpClient} from '@angular/common/http';
import { PersonsComponent } from './persons/persons.component';
import { PersonEditorComponent } from './persons/person-editor.component';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title: string;
  items: MenuItem[];

  constructor(private http: HttpClient) {
  }

  ngOnInit() {
        this.http.get<ClientInit>('/api/init').subscribe(data => {
          this.title = data.title;
        });
    }


}

interface ClientInit {
  title: string;
}
