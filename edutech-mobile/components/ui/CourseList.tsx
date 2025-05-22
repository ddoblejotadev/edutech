import React from 'react';
import { 
  FlatList, 
  StyleSheet, 
  View, 
  RefreshControl,
  ListRenderItemInfo
} from 'react-native';
import { Card } from './Card';
import { ThemedText } from '../ThemedText';
import { useColorScheme } from '@/hooks/useColorScheme';
import { Colors } from '@/constants/Colors';

export interface CourseItem {
  id: string;
  title: string;
  description?: string;
  imageUri?: string;
  instructor?: string;
  progress?: number;
  duration?: string;
  category?: string;
}

interface CourseListProps {
  data: CourseItem[];
  isLoading?: boolean;
  onRefresh?: () => void;
  onCoursePress?: (course: CourseItem) => void;
  emptyMessage?: string;
  horizontal?: boolean;
  compact?: boolean;
  title?: string;
  showCategory?: boolean;
  showInstructor?: boolean;
}

export function CourseList({
  data,
  isLoading = false,
  onRefresh,
  onCoursePress,
  emptyMessage = 'No hay cursos disponibles',
  horizontal = false,
  compact = false,
  title,
  showCategory = true,
  showInstructor = true,
}: CourseListProps) {
  const colorScheme = useColorScheme();
  const colors = Colors[colorScheme ?? 'light'];
  const refreshColor = colors.tint;

  const renderCourse = ({ item }: ListRenderItemInfo<CourseItem>) => (
    <Card
      title={item.title}
      subtitle={showInstructor ? `Instructor: ${item.instructor}` : undefined}
      description={item.description}
      imageUri={item.imageUri}
      badge={showCategory ? item.category : undefined}
      progress={item.progress}
      compact={compact}
      onPress={() => onCoursePress && onCoursePress(item)}
      style={horizontal ? styles.horizontalCard : {}}
    />
  );

  const EmptyList = () => (
    <View style={styles.emptyContainer}>
      <ThemedText type="body" secondary={true} style={styles.emptyText}>
        {emptyMessage}
      </ThemedText>
    </View>
  );

  return (
    <View style={styles.container}>
      {title && (
        <ThemedText type="heading2" style={styles.title}>
          {title}
        </ThemedText>
      )}

      <FlatList
        data={data}
        renderItem={renderCourse}
        keyExtractor={(item) => item.id}
        contentContainerStyle={[
          styles.listContent,
          data.length === 0 && styles.emptyListContent,
        ]}
        horizontal={horizontal}
        showsHorizontalScrollIndicator={false}
        showsVerticalScrollIndicator={false}
        refreshControl={
          onRefresh ? (
            <RefreshControl refreshing={isLoading} onRefresh={onRefresh} colors={[refreshColor]} />
          ) : undefined
        }
        ListEmptyComponent={EmptyList}
        ItemSeparatorComponent={
          horizontal ? () => <View style={styles.horizontalSeparator} /> : undefined
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  title: {
    marginBottom: 16,
    paddingHorizontal: 16,
  },
  listContent: {
    paddingHorizontal: 16,
    paddingBottom: 16,
  },
  emptyListContent: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 24,
  },
  emptyText: {
    textAlign: 'center',
  },
  horizontalCard: {
    width: 280,
  },
  horizontalSeparator: {
    width: 16,
  },
});
