package com.example.bicycleinsurance.groovy

class BaseScript extends Script {

	@Override
	Object run() {
		return null
	}

	ArrayList<LinkedHashMap<String, Serializable>> getAgeFactorData() {
		return [
				['MAKE': 'Canyon', 'MODEL': 'CF 5', 'VALUE_FROM': 0.0G, 'VALUE_TO': 5.0G, 'FACTOR_MIN': 1.5G, 'FACTOR_MAX': 2.0G],
				['MAKE': 'Canyon', 'MODEL': 'CF 5', 'VALUE_FROM': 6.0G, 'VALUE_TO': 10.0G, 'FACTOR_MIN': 1.2G, 'FACTOR_MAX': 1.4G],
				['MAKE': 'Canyon', 'MODEL': 'CF 5', 'VALUE_FROM': 11.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 0.9G, 'FACTOR_MAX': 1.1G],
				['MAKE': 'Whyte', 'MODEL': 'T-160 RS', 'VALUE_FROM': 0.0G, 'VALUE_TO': 4.0G, 'FACTOR_MIN': 1.6G, 'FACTOR_MAX': 2.05G],
				['MAKE': 'Whyte', 'MODEL': 'T-160 RS', 'VALUE_FROM': 5.0G, 'VALUE_TO': 10.0G, 'FACTOR_MIN': 1.2G, 'FACTOR_MAX': 1.5G],
				['MAKE': 'Whyte', 'MODEL': 'T-160 RS', 'VALUE_FROM': 11.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 0.9G, 'FACTOR_MAX': 1.1G],
				['MAKE': 'Pearl', 'MODEL': 'Gravel SL EVO', 'VALUE_FROM': 0.0G, 'VALUE_TO': 2.0G, 'FACTOR_MIN': 2.1G, 'FACTOR_MAX': 2.5G],
				['MAKE': 'Pearl', 'MODEL': 'Gravel SL EVO', 'VALUE_FROM': 3.0G, 'VALUE_TO': 6.0G, 'FACTOR_MIN': 1.5G, 'FACTOR_MAX': 2.0G],
				['MAKE': 'Pearl', 'MODEL': 'Gravel SL EVO', 'VALUE_FROM': 7.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 0.9G, 'FACTOR_MAX': 1.4G],
				['MAKE': 'Whyte', 'VALUE_FROM': 0.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 0.95G, 'FACTOR_MAX': 1.6G],
				['MAKE': 'Canyon', 'VALUE_FROM': 0.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 0.95G, 'FACTOR_MAX': 1.6G],
				['MAKE': 'Pearl', 'VALUE_FROM': 0.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 0.99G, 'FACTOR_MAX': 1.8G],
				['MAKE': 'Krush', 'VALUE_FROM': 0.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 0.93G, 'FACTOR_MAX': 1.75G],
				['MAKE': 'Megamo', 'VALUE_FROM': 0.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 1.1G, 'FACTOR_MAX': 2.3G],
				['MAKE': 'Sensa', 'VALUE_FROM': 0.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 0.8G, 'FACTOR_MAX': 2.5G],
				['VALUE_FROM': 0.0G, 'VALUE_TO': 15.0G, 'FACTOR_MIN': 1.0G, 'FACTOR_MAX': 3.0G]
		]
	}

	ArrayList<LinkedHashMap<String, Serializable>> getRiskCountFactorData() {
		return [
				['VALUE_FROM': 0.0G, 'VALUE_TO': 1.0G, 'FACTOR_MIN': 1.3G, 'FACTOR_MAX': 1.3G],
				['VALUE_FROM': 2.0G, 'VALUE_TO': 3.0G, 'FACTOR_MIN': 1.2G, 'FACTOR_MAX': 1.2G],
				['VALUE_FROM': 4.0G, 'VALUE_TO': 5.0G, 'FACTOR_MIN': 1.1G, 'FACTOR_MAX': 1.1G],
				['VALUE_FROM': 6.0G, 'VALUE_TO': 10.0G, 'FACTOR_MIN': 1.0G, 'FACTOR_MAX': 1.0G]
		]
	}

	ArrayList<LinkedHashMap<String, Serializable>> getSumInsuredFactorData() {
		return [
				['VALUE_FROM': 100.0G, 'VALUE_TO': 1000.0G, 'FACTOR_MIN': 0.5G, 'FACTOR_MAX': 1.0G],
				['VALUE_FROM': 1001.0G, 'VALUE_TO': 3000.0G, 'FACTOR_MIN': 1.0G, 'FACTOR_MAX': 2.0G],
				['VALUE_FROM': 3001.0G, 'VALUE_TO': 5000.0G, 'FACTOR_MIN': 2.0G, 'FACTOR_MAX': 3.0G]
		]
	}

	ArrayList<LinkedHashMap<String, Serializable>> getRiskBasePremiumData() {
		return [
				['RISK_TYPE': 'DAMAGE', 'PREMIUM': 10.0G],
				['RISK_TYPE': 'THIRD_PARTY_DAMAGE', 'PREMIUM': 20.0G],
				['RISK_TYPE': 'THEFT', 'PREMIUM': 30.0G]
		]
	}

	def riskBasePremium(String riskType) {
		def premiumData = getRiskBasePremiumData().find { it['RISK_TYPE'] == riskType }
		if (premiumData) {
			return premiumData['PREMIUM'] as BigDecimal
		} else {
			throw new IllegalArgumentException("Unsupported risk type: $riskType")
		}
	}

	def calculateSumInsuredFactor(BigDecimal sumInsured) {
		def factorData = getSumInsuredFactorData().find { sumInsured >= it['VALUE_FROM'] && sumInsured <= it['VALUE_TO'] }
		if (factorData) {
			def factorMax = factorData['FACTOR_MAX'] as BigDecimal
			def factorMin = factorData['FACTOR_MIN'] as BigDecimal
			def valueFrom = factorData['VALUE_FROM'] as BigDecimal
			def valueTo = factorData['VALUE_TO'] as BigDecimal
			def factor = factorMax - (factorMax - factorMin) * (valueTo - sumInsured) / (valueTo - valueFrom)
			return factor
		} else {
			throw new IllegalArgumentException("No sum insured factor data found for sum insured: $sumInsured")
		}
	}

	def calculateRiskCountFactor(int riskCount) {
		def factorData = getRiskCountFactorData().find { riskCount >= it['VALUE_FROM'] && riskCount <= it['VALUE_TO'] }
		if (factorData) {
			def factorMax = factorData['FACTOR_MAX'] as BigDecimal
			def factorMin = factorData['FACTOR_MIN'] as BigDecimal
			def valueFrom = factorData['VALUE_FROM'] as BigDecimal
			def valueTo = factorData['VALUE_TO'] as BigDecimal
			def factor = factorMax - (factorMax - factorMin) * (valueTo - riskCount) / (valueTo - valueFrom)
			return factor
		} else {
			throw new IllegalArgumentException("No risk count factor data found for risk count: $riskCount")
		}
	}

	def calculateAgeFactor(String make, String model, int age) {
		def factorDataList = getAgeFactorData()
		def factorData = factorDataList.find {
			it.containsKey('MAKE') && it.containsKey('MODEL') &&
					it['MAKE'] == make && it['MODEL'] == model &&
					age >= it['VALUE_FROM'] && age <= it['VALUE_TO']
		}

		if (!factorData) {
			factorData = factorDataList.find {
				it.containsKey('MAKE') && !it.containsKey('MODEL') &&
						it['MAKE'] == make &&
						age >= it['VALUE_FROM'] && age <= it['VALUE_TO']
			}
		}

		if (!factorData) {
			factorData = factorDataList.find {
				!it.containsKey('MAKE') && !it.containsKey('MODEL') &&
						age >= it['VALUE_FROM'] && age <= it['VALUE_TO']
			}
		}

		if (factorData) {
			def factorMax = factorData['FACTOR_MAX'] as BigDecimal
			def factorMin = factorData['FACTOR_MIN'] as BigDecimal
			def valueFrom = factorData['VALUE_FROM'] as BigDecimal
			def valueTo = factorData['VALUE_TO'] as BigDecimal
			def factor = factorMax - (factorMax - factorMin) * (valueTo - age) / (valueTo - valueFrom)
			return factor
		} else {
			throw new IllegalArgumentException("No age factor data found for make: $make, model: $model, age: $age")
		}
	}
}