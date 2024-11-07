class BaseScript extends Script {

	@Override
	Object run() {
		return null
	}

	ArrayList<LinkedHashMap<String, Serializable>> getAgeFactorData() {
		return [['MAKE': 'Canyon', 'MODEL': 'CF 5', 'VALUE_FROM': 0, 'VALUE_TO': 5, 'FACTOR_MIN': 1.5, 'FACTOR_MAX': 2],
				['MAKE': 'Canyon', 'MODEL': 'CF 5', 'VALUE_FROM': 6, 'VALUE_TO': 10, 'FACTOR_MIN': 1.2, 'FACTOR_MAX': 1.4],
				['MAKE': 'Canyon', 'MODEL': 'CF 5', 'VALUE_FROM': 11, 'VALUE_TO': 15, 'FACTOR_MIN': 0.9, 'FACTOR_MAX': 1.1],
				['MAKE': 'Canyon', 'VALUE_FROM': 0, 'VALUE_TO': 15, 'FACTOR_MIN': 0.95, 'FACTOR_MAX': 1.6],
				['MAKE': 'Whyte', 'MODEL': 'T-160 RS', 'VALUE_FROM': 0, 'VALUE_TO': 4, 'FACTOR_MIN': 1.6, 'FACTOR_MAX': 2.05],
				['MAKE': 'Whyte', 'MODEL': 'T-160 RS', 'VALUE_FROM': 5, 'VALUE_TO': 10, 'FACTOR_MIN': 1.2, 'FACTOR_MAX': 1.5],
				['MAKE': 'Whyte', 'MODEL': 'T-160 RS', 'VALUE_FROM': 11, 'VALUE_TO': 15, 'FACTOR_MIN': 0.9, 'FACTOR_MAX': 1.1],
				['MAKE': 'Whyte', 'VALUE_FROM': 0, 'VALUE_TO': 15, 'FACTOR_MIN': 0.95, 'FACTOR_MAX': 1.6],
				['MAKE': 'Pearl', 'MODEL': 'Gravel SL EVO', 'VALUE_FROM': 0, 'VALUE_TO': 2, 'FACTOR_MIN': 2.1, 'FACTOR_MAX': 2.5],
				['MAKE': 'Pearl', 'MODEL': 'Gravel SL EVO', 'VALUE_FROM': 3, 'VALUE_TO': 6, 'FACTOR_MIN': 1.5, 'FACTOR_MAX': 2],
				['MAKE': 'Pearl', 'MODEL': 'Gravel SL EVO', 'VALUE_FROM': 7, 'VALUE_TO': 15, 'FACTOR_MIN': 0.9, 'FACTOR_MAX': 1.4],
				['MAKE': 'Pearl', 'VALUE_FROM': 0, 'VALUE_TO': 15, 'FACTOR_MIN': 0.99, 'FACTOR_MAX': 1.8],
				['MAKE': 'Krush', 'VALUE_FROM': 0, 'VALUE_TO': 15, 'FACTOR_MIN': 0.93, 'FACTOR_MAX': 1.75],
				['MAKE': 'Megamo', 'VALUE_FROM': 0, 'VALUE_TO': 15, 'FACTOR_MIN': 1.1, 'FACTOR_MAX': 2.3],
				['MAKE': 'Sensa', 'VALUE_FROM': 0, 'VALUE_TO': 15, 'FACTOR_MIN': 0.8, 'FACTOR_MAX': 2.5],
				['VALUE_FROM': 0, 'VALUE_TO': 15, 'FACTOR_MIN': 1, 'FACTOR_MAX': 3]]
	}

	ArrayList<LinkedHashMap<String, Serializable>> getRiskCountFactorData() {
		return [
				['VALUE_FROM': 0, 'VALUE_TO': 1, 'FACTOR_MIN': 1.3, 'FACTOR_MAX': 1.3],
				['VALUE_FROM': 2, 'VALUE_TO': 3, 'FACTOR_MIN': 1.2, 'FACTOR_MAX': 1.2],
				['VALUE_FROM': 4, 'VALUE_TO': 5, 'FACTOR_MIN': 1.1, 'FACTOR_MAX': 1.1],
				['VALUE_FROM': 6, 'VALUE_TO': 10, 'FACTOR_MIN': 1, 'FACTOR_MAX': 1]
		]
	}

	ArrayList<LinkedHashMap<String, Serializable>> getSumInsuredFactorData() {
		return [
				['VALUE_FROM': 100, 'VALUE_TO': 1000, 'FACTOR_MIN': 0.5, 'FACTOR_MAX': 1],
				['VALUE_FROM': 1001, 'VALUE_TO': 3000, 'FACTOR_MIN': 1, 'FACTOR_MAX': 2],
				['VALUE_FROM': 3001, 'VALUE_TO': 5000, 'FACTOR_MIN': 2, 'FACTOR_MAX': 3]

		]
	}

	ArrayList<LinkedHashMap<String, Serializable>> getRiskBasePremiumData() {
		return [
				['RISK_TYPE': 'DAMAGE', 'PREMIUM': 10],
				['RISK_TYPE': 'THIRD_PARTY_DAMAGE', 'PREMIUM': 20],
				['RISK_TYPE': 'THEFT', 'PREMIUM': 30]
		]
	}
}
