import { BaseEntity } from './../../shared';

export class SmartCard implements BaseEntity {
    constructor(
        public id?: number,
        public cardBalance?: number,
        public issuedDate?: string,
        public cardNumber?: string,
        public journeys?: BaseEntity[],
        public payments?: BaseEntity[],
        public recharges?: BaseEntity[],
    ) {
    }
}
