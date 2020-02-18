import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { InsurancepartnersgatewaySharedModule } from 'app/shared/shared.module';

import { DocsComponent } from './docs.component';

import { docsRoute } from './docs.route';

@NgModule({
  imports: [InsurancepartnersgatewaySharedModule, RouterModule.forChild([docsRoute])],
  declarations: [DocsComponent]
})
export class DocsModule {}
