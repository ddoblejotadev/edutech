import { StyleSheet } from 'react-native';

export const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  scrollView: {
    flex: 1,
  },
  header: {
    height: 250,
  },
  headerImage: {
    width: '100%',
    height: '100%',
  },
  backButton: {
    position: 'absolute',
    top: 16,
    left: 16,
    width: 36,
    height: 36,
    borderRadius: 18,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.3)',
  },
  contentContainer: {
    flex: 1,
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    marginTop: -24,
    paddingTop: 24,
    paddingHorizontal: 16,
  },
  titleRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 16,
  },
  titleWrapper: {
    flex: 1,
    paddingRight: 16,
  },
  enrollButtonContainer: {
    width: 50,
  },
  badgeContainer: {
    flexDirection: 'row',
    marginBottom: 8,
  },
  badge: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 4,
    marginRight: 8,
  },
  badgeText: {
    fontSize: 10,
    fontWeight: '500',
  },
  instructorContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 16,
  },
  instructorImage: {
    width: 36,
    height: 36,
    borderRadius: 18,
    marginRight: 12,
  },
  infoContainer: {
    flexDirection: 'row',
    marginBottom: 24,
    paddingVertical: 16,
    borderRadius: 12,
  },
  infoItem: {
    flex: 1,
    alignItems: 'center',
  },
  infoItemSeparator: {
    width: 1,
    height: '100%',
  },
  infoIcon: {
    marginBottom: 4,
  },
  infoValue: {
    marginBottom: 2,
  },
  sectionTitle: {
    marginBottom: 8,
  },
  descriptionText: {
    marginBottom: 24,
  },
  programContainer: {
    marginBottom: 24,
  },
  moduleItem: {
    borderLeftWidth: 2,
    paddingLeft: 16,
    marginBottom: 16,
  },
  moduleTitle: {
    marginBottom: 8,
  },
  lectureContainer: {
    marginVertical: 4,
  },
  lectureItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 8,
  },
  lectureIcon: {
    marginRight: 8,
  },
  lectureText: {
    flex: 1,
  },
  buttonContainer: {
    paddingHorizontal: 16,
    paddingVertical: 12,
    marginBottom: 24,
  },
  enrolledTag: {
    position: 'absolute',
    top: 16,
    right: 16,
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 20,
  },
  loaderContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 24,
  },
});
