import { BaseEntity } from './../../shared';

export class Recharge implements BaseEntity {
    constructor(
        public id?: number,
        public rechargeMethod?: string,
        public rechargeAmount?: number,
        public accountNumber?: string,
        public cardType?: string,
        public expDate?: string,
        public smartCard?: BaseEntity,
    ) {
    }
}
