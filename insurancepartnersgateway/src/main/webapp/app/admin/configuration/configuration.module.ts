import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { InsurancepartnersgatewaySharedModule } from 'app/shared/shared.module';

import { ConfigurationComponent } from './configuration.component';

import { configurationRoute } from './configuration.route';

@NgModule({
  imports: [InsurancepartnersgatewaySharedModule, RouterModule.forChild([configurationRoute])],
  declarations: [ConfigurationComponent]
})
export class ConfigurationModule {}
