import defaultSettings from '@/settings'

const title = defaultSettings.title || 'Data Visualization System'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
