import React, { useState, useEffect, useContext } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  SafeAreaView,
  TouchableOpacity,
  RefreshControl,
  ActivityIndicator,
  Alert
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS, SPACING, FONT_SIZE, FONT_WEIGHT } from '../../config/theme';
import { Card } from '../../components/common/UIComponents';
import { AuthContext } from '../../context/AuthContext';

const FinancialStatusScreen = ({ navigation }) => {
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [financialData, setFinancialData] = useState(null);
  const [error, setError] = useState(null);
  const { user } = useContext(AuthContext);

  // Datos de demostración para estado financiero
  const demoFinancialData = {
    estadoCuenta: {
      saldoTotal: -125000,
      proximoVencimiento: {
        fecha: '2024-06-15',
        monto: 450000,
        concepto: 'Arancel Junio 2024'
      },
      cuotasPendientes: 2,
      ultimoPago: {
        fecha: '2024-05-15',
        monto: 450000,
        metodo: 'Transferencia bancaria'
      }
    },
    historialPagos: [
      {
        id: 1,
        fecha: '2024-05-15',
        concepto: 'Arancel Mayo 2024',
        monto: 450000,
        estado: 'Pagado',
        metodoPago: 'Transferencia bancaria'
      },
      {
        id: 2,
        fecha: '2024-04-15',
        concepto: 'Arancel Abril 2024',
        monto: 450000,
        estado: 'Pagado',
        metodoPago: 'Tarjeta de crédito'
      },
      {
        id: 3,
        fecha: '2024-03-15',
        concepto: 'Arancel Marzo 2024',
        monto: 450000,
        estado: 'Pagado',
        metodoPago: 'Efectivo'
      }
    ],
    becas: [
      {
        id: 1,
        nombre: 'Beca de Excelencia Académica DUOC UC',
        estado: 'ACTIVA',
        porcentaje: 50,
        descripcion: 'Beca por rendimiento académico destacado',
        montoDescuento: 225000
      },
      {
        id: 2,
        nombre: 'Gratuidad',
        estado: 'ACTIVA',
        porcentaje: 100,
        descripcion: 'Beneficio estatal de gratuidad',
        montoDescuento: 450000
      }
    ]
  };

  useEffect(() => {
    loadFinancialData();
  }, []);

  const loadFinancialData = async () => {
    try {
      setError(null);
      // Simular carga de datos
      await new Promise(resolve => setTimeout(resolve, 1000));
      setFinancialData(demoFinancialData);
    } catch (error) {
      console.error('Error cargando datos financieros:', error);
      setError('No se pudieron cargar los datos financieros');
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  const onRefresh = () => {
    setRefreshing(true);
    loadFinancialData();
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('es-CL', {
      style: 'currency',
      currency: 'CLP',
      minimumFractionDigits: 0
    }).format(amount);
  };

  const getPaymentStatusColor = (status) => {
    switch (status) {
      case 'Pagado': return COLORS.success;
      case 'Pendiente': return COLORS.warning;
      case 'Vencido': return COLORS.error;
      default: return COLORS.text;
    }
  };

  const handlePayment = () => {
    Alert.alert(
      'Realizar Pago',
      'Esta funcionalidad estará disponible próximamente',
      [{ text: 'Entendido', style: 'default' }]
    );
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color={COLORS.primary} />
        <Text style={styles.loadingText}>Cargando estado financiero...</Text>
      </View>
    );
  }

  if (error) {
    return (
      <SafeAreaView style={styles.container}>
        <View style={styles.header}>
          <TouchableOpacity 
            style={styles.backButton}
            onPress={() => navigation.goBack()}
          >
            <Ionicons name="arrow-back" size={24} color={COLORS.white} />
          </TouchableOpacity>
          <Text style={styles.headerTitle}>Estado Financiero</Text>
          <View style={styles.placeholder} />
        </View>
        
        <View style={styles.errorContainer}>
          <Ionicons name="card-outline" size={64} color={COLORS.error} />
          <Text style={styles.errorText}>{error}</Text>
          <TouchableOpacity style={styles.retryButton} onPress={loadFinancialData}>
            <Text style={styles.retryText}>Reintentar</Text>
          </TouchableOpacity>
        </View>
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <TouchableOpacity 
          style={styles.backButton}
          onPress={() => navigation.goBack()}
        >
          <Ionicons name="arrow-back" size={24} color={COLORS.white} />
        </TouchableOpacity>
        <Text style={styles.headerTitle}>Estado Financiero</Text>
        <View style={styles.placeholder} />
      </View>

      <ScrollView 
        style={styles.content}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {/* Resumen de cuenta */}
        <Card style={styles.summaryCard}>
          <View style={styles.summaryHeader}>
            <Ionicons name="card-outline" size={24} color={COLORS.primary} />
            <Text style={styles.summaryTitle}>Resumen de Cuenta</Text>
          </View>
          
          <View style={styles.balanceContainer}>
            <Text style={styles.balanceLabel}>Saldo Total</Text>
            <Text style={[
              styles.balanceAmount,
              { color: financialData.estadoCuenta.saldoTotal < 0 ? COLORS.error : COLORS.success }
            ]}>
              {formatCurrency(financialData.estadoCuenta.saldoTotal)}
            </Text>
          </View>
          
          {financialData.estadoCuenta.saldoTotal < 0 && (
            <View style={styles.alertBanner}>
              <Ionicons name="warning-outline" size={20} color={COLORS.warning} />
              <Text style={styles.alertText}>
                Tienes cuotas pendientes de pago
              </Text>
            </View>
          )}
        </Card>

        {/* Próximo vencimiento */}
        <Card style={styles.nextPaymentCard}>
          <View style={styles.nextPaymentHeader}>
            <Ionicons name="time-outline" size={24} color={COLORS.warning} />
            <Text style={styles.nextPaymentTitle}>Próximo Vencimiento</Text>
          </View>
          
          <View style={styles.nextPaymentInfo}>
            <Text style={styles.nextPaymentConcept}>
              {financialData.estadoCuenta.proximoVencimiento.concepto}
            </Text>
            <Text style={styles.nextPaymentDate}>
              Fecha: {new Date(financialData.estadoCuenta.proximoVencimiento.fecha).toLocaleDateString('es-CL')}
            </Text>
            <Text style={styles.nextPaymentAmount}>
              {formatCurrency(financialData.estadoCuenta.proximoVencimiento.monto)}
            </Text>
          </View>
          
          <TouchableOpacity style={styles.payButton} onPress={handlePayment}>
            <Ionicons name="card" size={20} color={COLORS.white} />
            <Text style={styles.payButtonText}>Pagar Ahora</Text>
          </TouchableOpacity>
        </Card>

        {/* Becas activas */}
        {financialData.becas && financialData.becas.length > 0 && (
          <Card style={styles.scholarshipsCard}>
            <View style={styles.scholarshipsHeader}>
              <Ionicons name="gift-outline" size={24} color={COLORS.success} />
              <Text style={styles.scholarshipsTitle}>Becas y Beneficios</Text>
            </View>
            
            {financialData.becas.map((beca) => (
              <View key={beca.id} style={styles.scholarshipItem}>
                <View style={styles.scholarshipInfo}>
                  <Text style={styles.scholarshipName}>{beca.nombre}</Text>
                  <Text style={styles.scholarshipDescription}>{beca.descripcion}</Text>
                  <Text style={styles.scholarshipDiscount}>
                    Descuento: {beca.porcentaje}% ({formatCurrency(beca.montoDescuento)})
                  </Text>
                </View>
                <View style={[styles.scholarshipStatus, 
                  { backgroundColor: beca.estado === 'ACTIVA' ? COLORS.success : COLORS.warning }
                ]}>
                  <Text style={styles.scholarshipStatusText}>{beca.estado}</Text>
                </View>
              </View>
            ))}
          </Card>
        )}

        {/* Historial de pagos */}
        <Card style={styles.historyCard}>
          <View style={styles.historyHeader}>
            <Ionicons name="receipt-outline" size={24} color={COLORS.primary} />
            <Text style={styles.historyTitle}>Historial de Pagos</Text>
          </View>
          
          {financialData.historialPagos.map((pago) => (
            <View key={pago.id} style={styles.paymentItem}>
              <View style={styles.paymentInfo}>
                <Text style={styles.paymentConcept}>{pago.concepto}</Text>
                <Text style={styles.paymentDate}>
                  {new Date(pago.fecha).toLocaleDateString('es-CL')}
                </Text>
                <Text style={styles.paymentMethod}>{pago.metodoPago}</Text>
              </View>
              
              <View style={styles.paymentAmountContainer}>
                <Text style={styles.paymentAmount}>
                  {formatCurrency(pago.monto)}
                </Text>
                <View style={[styles.paymentStatus, 
                  { backgroundColor: getPaymentStatusColor(pago.estado) }
                ]}>
                  <Text style={styles.paymentStatusText}>{pago.estado}</Text>
                </View>
              </View>
            </View>
          ))}
        </Card>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.primary,
    padding: SPACING.md,
    paddingTop: SPACING.xl,
  },
  backButton: {
    marginRight: SPACING.md,
  },
  headerTitle: {
    fontSize: FONT_SIZE.lg,
    color: COLORS.white,
    fontWeight: FONT_WEIGHT.bold,
    flex: 1,
  },
  placeholder: {
    width: 24,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: COLORS.background,
  },
  loadingText: {
    marginTop: SPACING.md,
    fontSize: FONT_SIZE.md,
    color: COLORS.text,
  },
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: SPACING.lg,
  },
  errorText: {
    fontSize: FONT_SIZE.md,
    color: COLORS.error,
    textAlign: 'center',
    marginVertical: SPACING.md,
  },
  retryButton: {
    backgroundColor: COLORS.primary,
    paddingHorizontal: SPACING.lg,
    paddingVertical: SPACING.sm,
    borderRadius: 8,
  },
  retryText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
  },
  content: {
    flex: 1,
    padding: SPACING.md,
  },
  summaryCard: {
    marginBottom: SPACING.md,
  },
  summaryHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  summaryTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginLeft: SPACING.sm,
  },
  balanceContainer: {
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  balanceLabel: {
    fontSize: FONT_SIZE.md,
    color: COLORS.textSecondary,
    marginBottom: SPACING.xs,
  },
  balanceAmount: {
    fontSize: FONT_SIZE.xxl,
    fontWeight: FONT_WEIGHT.bold,
  },
  alertBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: `${COLORS.warning}22`,
    padding: SPACING.sm,
    borderRadius: 8,
  },
  alertText: {
    color: COLORS.warning,
    marginLeft: SPACING.sm,
    fontSize: FONT_SIZE.sm,
  },
  nextPaymentCard: {
    marginBottom: SPACING.md,
  },
  nextPaymentHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  nextPaymentTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginLeft: SPACING.sm,
  },
  nextPaymentInfo: {
    marginBottom: SPACING.md,
  },
  nextPaymentConcept: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  nextPaymentDate: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginBottom: SPACING.xs,
  },
  nextPaymentAmount: {
    fontSize: FONT_SIZE.xl,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.warning,
  },
  payButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: COLORS.primary,
    padding: SPACING.md,
    borderRadius: 8,
  },
  payButtonText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    marginLeft: SPACING.sm,
  },
  scholarshipsCard: {
    marginBottom: SPACING.md,
  },
  scholarshipsHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  scholarshipsTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginLeft: SPACING.sm,
  },
  scholarshipItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    paddingVertical: SPACING.sm,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  scholarshipInfo: {
    flex: 1,
    marginRight: SPACING.md,
  },
  scholarshipName: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  scholarshipDescription: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginBottom: SPACING.xs,
  },
  scholarshipDiscount: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.success,
    fontWeight: FONT_WEIGHT.medium,
  },
  scholarshipStatus: {
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: 4,
  },
  scholarshipStatusText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.bold,
  },
  historyCard: {
    marginBottom: SPACING.md,
  },
  historyHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: SPACING.md,
  },
  historyTitle: {
    fontSize: FONT_SIZE.lg,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginLeft: SPACING.sm,
  },
  paymentItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    paddingVertical: SPACING.md,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
  },
  paymentInfo: {
    flex: 1,
    marginRight: SPACING.md,
  },
  paymentConcept: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.medium,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  paymentDate: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
    marginBottom: SPACING.xs,
  },
  paymentMethod: {
    fontSize: FONT_SIZE.sm,
    color: COLORS.textSecondary,
  },
  paymentAmountContainer: {
    alignItems: 'flex-end',
  },
  paymentAmount: {
    fontSize: FONT_SIZE.md,
    fontWeight: FONT_WEIGHT.bold,
    color: COLORS.text,
    marginBottom: SPACING.xs,
  },
  paymentStatus: {
    paddingHorizontal: SPACING.sm,
    paddingVertical: SPACING.xs,
    borderRadius: 4,
  },
  paymentStatusText: {
    color: COLORS.white,
    fontSize: FONT_SIZE.xs,
    fontWeight: FONT_WEIGHT.bold,
  },
});

export default FinancialStatusScreen;