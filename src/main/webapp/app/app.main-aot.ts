import { platformBrowser } from '@angular/platform-browser';
import { ProdConfig } from './blocks/config/prod.config';
import { TicketingSystemAppModuleNgFactory } from '../../../../build/aot/src/main/webapp/app/app.module.ngfactory';

ProdConfig();

platformBrowser().bootstrapModuleFactory(TicketingSystemAppModuleNgFactory)
.then((success) => console.log(`Application started`))
.catch((err) => console.error(err));
