import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';

import {DropdownModule, ButtonModule, MenubarModule, DataTableModule,
SharedModule, MessagesModule, GrowlModule, PanelModule, ConfirmDialogModule} from 'primeng/primeng';
import {ConfirmationService} from 'primeng/primeng';

import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './page-not-found.component';
import { PersonsComponent } from './persons/persons.component';
import { PersonsService } from './persons/persons.service';
import { PersonEditorComponent } from './persons/person-editor.component';
import { PersonDetailComponent } from './persons/person-detail.component';

const appRoutes: Routes = [
  { path: 'persons', component: PersonsComponent },
  { path: 'persons/new', component: PersonEditorComponent },
  { path: 'persons/:id', component: PersonDetailComponent },
  { path: 'persons/edit/:id', component: PersonEditorComponent },
  { path: '',
    redirectTo: '/persons',
    pathMatch: 'full'
  },
  { path: '**', component: PageNotFoundComponent }
];


@NgModule({
  declarations: [
    AppComponent,
    PersonsComponent,
    PersonEditorComponent,
    PersonDetailComponent,
    PageNotFoundComponent
  ],
  imports: [
    RouterModule.forRoot(
      appRoutes,
     // { enableTracing: true } // <-- debugging purposes only
    ),
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    BrowserAnimationsModule,
    DropdownModule,
    ButtonModule,
    DataTableModule,
    SharedModule,
    MenubarModule,
    GrowlModule,
    ConfirmDialogModule,
    PanelModule
  ],
  providers: [ConfirmationService, PersonsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
